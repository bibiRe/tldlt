package com.sysware.tldlt.app.service.devicetypecheckitem;

import java.util.List;

import org.g4studio.core.metatype.Dto;
import org.g4studio.system.common.util.SystemConstants;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：DeviceTypeCheckItemServiceImpl
 * Descript：设备类型检查项服务实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月18日 上午9:40:05
 * Version：@version
 */
public class DeviceTypeCheckItemServiceImpl extends BaseAppServiceImpl
        implements DeviceTypeCheckItemService {

    @SuppressWarnings("unchecked")
    @Override
    public Dto addInfo(Dto inDto) {
        Dto outDto = checkAddInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        outDto = DtoUtils.addInfoAndCheckIntIdFail(appDao,
                "App.DeviceTypeCheckItem.addInfo", inDto,
                "devicecheckcontentid", "设备类型检查项");
        if (null != outDto) {
            return outDto;
        }

        Dto result = DtoUtils.getSuccessRetDto("设备类型检查项数据新增成功");
        result.put("devicecheckcontentid",
                inDto.getAsInteger("devicecheckcontentid"));
        return result;
    }

    /**
     * 检查设备类型检查项动作.
     * @param inDto 输入dto
     * @return dto.
     */
    private Dto checkAction(Dto inDto) {
        Integer action = inDto.getAsInteger("action");
        if (null == action) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备类型检查项动作不存在");
        }
        if (!DtoUtils.checkDictCodeExist("DEVCHKACTION", action.toString())) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "设备类型检查项动作无效");
        }
        return null;
    }

    /**
     * 检查新增信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回null
     */
    private Dto checkAddInfo(Dto inDto) {
        Dto outDto = DtoUtils.checkDtoIntId(inDto, "devicecheckcontentid");
        if (null == outDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "记录编号不为空");
        }
        outDto = checkInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        return null;
    }

    /**
     * 检查输入信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回空
     */
    private Dto checkInfo(Dto inDto) {
        String name = inDto.getAsString("checkitemname");
        if (AppTools.isBlankString(name)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备类型检查项名称为空");
        }
        String devicetypeid = inDto.getAsString("devicetypeid");
        if (AppTools.isEmptyString(devicetypeid)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "所属设备类型为空");
        }
        Dto outDto = (Dto) appDao.queryForObject(
                "App.DeviceType.queryDeviceTypeInfoById", devicetypeid);
        if (null == outDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "没有此设备类型");
        }

        outDto = checkAction(inDto);
        if (null != outDto) {
            return outDto;
        }
        outDto = checkState(inDto);
        if (null != outDto) {
            return outDto;
        }
        return null;
    }

    /**
     * 检查设备类型检查项状态.
     * @param inDto 输入dto
     * @return dto.
     */
    @SuppressWarnings("unchecked")
    private Dto checkState(Dto inDto) {
        String isOK = inDto.getAsString("state");
        if (AppTools.isEmptyString(isOK)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "state为空");
        }
        if (!SystemConstants.ENABLED_Y.equals(isOK)) {
            inDto.put("state", SystemConstants.ENABLED_N);
        } else {
            inDto.put("state", SystemConstants.ENABLED_Y);
        }
        return null;
    }

    /**
     * 检查更新输入信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回空
     */
    private Dto checkUpdateInfo(Dto inDto) {
        Dto outDto = DtoUtils.checkDtoIntIdExist(appDao,
                "App.DeviceTypeCheckItem.queryCheckItemById", inDto,
                "devicecheckcontentid");
        if (null != outDto) {
            return outDto;
        }
        outDto = checkInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        return null;
    }

    /**
     * 删除编号.
     * @param id 编号.
     * @param outDto Dto对象
     */
    private void deleteId(int id, BaseRetDto outDto) {
        int counta = ((Integer) appDao.queryForObject(
                "App.DeviceTypeCheckItem.queryCountInspectPlanDeviceById", id))
                .intValue();
        if (counta > 0) {
            appDao.update("App.DeviceTypeCheckItem.updateStateDisableInfo", id);
        } else {
            appDao.delete("App.DeviceTypeCheckItem.deleteInfo", id);
        }
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
            int id = Integer.parseInt(ids);
            deleteId(id, outDto);
            if (!outDto.isRetSuccess()) {
                break;
            }
        }
        return outDto;
    }

    @Override
    public Dto updateInfo(Dto inDto) {
        Dto outDto = checkUpdateInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        if (appDao.update("App.DeviceTypeCheckItem.updateInfo", inDto) < 1) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_UPDATE_FAIL,
                    "检查项更新数据失败");
        } else {
            return DtoUtils.getSuccessRetDto("检查项数据更新成功");
        }
    }
}
