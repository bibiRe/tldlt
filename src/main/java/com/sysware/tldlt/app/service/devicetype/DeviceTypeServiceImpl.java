package com.sysware.tldlt.app.service.devicetype;

import java.util.Collection;
import java.util.List;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：DeviceTypeServiceImpl
 * Descript：设备类型服务实现类.
 * Create：SW-ITS-HHE Create
 * Time：2015年7月31日 上午11:03:01 Version：@version
 */
public class DeviceTypeServiceImpl extends BaseAppServiceImpl implements
        DeviceTypeService {

    @Override
    public Dto addInfo(Dto inDto) {
        Dto outDto = checkAddInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        appDao.insert("App.DeviceType.addInfo", inDto);
        return DtoUtils.getSuccessRetDto("设备类型数据新增成功");
    }

    /**
     * 检查新增信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回null
     */
    private Dto checkAddInfo(Dto inDto) {
        Dto outDto = checkInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        if (checkDeviceTypeIdExist(inDto.getAsString("devicetypeid"))) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_REPEAT_VALUE,
                    "设备类型编号已存在");
        }
        return null;
    }

    /**
     * 检查设备编号是否存在.
     * @param id 编号
     * @return 是否存在
     */
    private boolean checkDeviceTypeIdExist(String id) {
        Object obj = appDao.queryForObject(
                "App.DeviceType.queryDeviceTypeInfoById", id);
        return null != obj;
    }

    /**
     * 检查输入信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回空
     */
    @SuppressWarnings("unchecked")
    private Dto checkInfo(Dto inDto) {
        String id = inDto.getAsString("devicetypeid");
        if (AppTools.isBlankString(id)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备类型编号为空");
        }
        String name = inDto.getAsString("devicetypename");
        if (AppTools.isBlankString(name)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备类型名称为空");
        }

        String pid = inDto.getAsString("parentid");
        if (!AppTools.isEmptyString(pid) && (!"0".equals(pid))) {
            if (!checkDeviceTypeIdExist(pid)) {
                return DtoUtils.getErrorRetDto(
                        AppCommon.RET_CODE_INVALID_VALUE, "上级设备类型无效");
            }
        } else {
            inDto.put("parentid", null);
        }

        return null;
    }

    /**
     * 检查更新输入信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回空
     */
    private Dto checkUpdateInfo(Dto inDto) {
        Dto outDto = checkInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        String oid = inDto.getAsString("oid");
        if (AppTools.isEmptyString(oid)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备类型原始编号为空");
        }
        if (!checkDeviceTypeIdExist(oid)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "原有的设备类型没有找到");
        }
        String deviceTypeId = inDto.getAsString("devicetypeid");
        if (!oid.equals(deviceTypeId) && checkDeviceTypeIdExist(deviceTypeId)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_REPEAT_VALUE,
                    "设备类型编号重复");
        }
        return null;
    }

    /**
     * 删除编号.
     * @param id 编号.
     * @param outDto Dto对象
     */
    @SuppressWarnings("unchecked")
    private void deleteId(String id, BaseRetDto outDto) {
        List<String> list = (List<String>) appDao.queryForList(
                "App.DeviceType.queryChildDeviceTypeIds", id);
        for (String ids : list) {
            deleteId(ids, outDto);
        }
        appDao.delete("App.DeviceType.deleteInfo", id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto deleteInfo(Dto inDto) {
        BaseRetDto outDto = new BaseRetDto();
        List<String> list = ((List<String>) inDto.getAsList("ids"));
        outDto.setRetSuccess();
        for (String ids : list) {
            if (AppTools.isEmptyString(ids)) {
                continue;
            }
            deleteId(ids, outDto);
            if (!outDto.isRetSuccess()) {
                break;
            }
        }
        return outDto;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Collection queryDeviceTypeItems(Dto dto) {
        List result = appDao.queryForList(
                "App.DeviceType.queryDeviceTypeItemsByDto", dto);
        return result;
    }

    @Override
    public Dto updateInfo(Dto inDto) {
        Dto outDto = checkUpdateInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        appDao.update("App.DeviceType.updateInfo", inDto);
        return DtoUtils.getSuccessRetDto("设备类型数据更新成功");
    }

}
