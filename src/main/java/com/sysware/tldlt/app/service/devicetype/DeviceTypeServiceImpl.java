package com.sysware.tldlt.app.service.devicetype;

import java.util.List;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：DeviceTypeServiceImpl Descript：设备类型服务实现类. Create：SW-ITS-HHE Create
 * Time：2015年7月31日 上午11:03:01 Version：@version
 */
public class DeviceTypeServiceImpl extends BaseAppServiceImpl implements DeviceTypeService {

	@Override
	public Dto addInfo(Dto inDto) {
		Dto outDto = checkAddInfo(inDto);
		if (null != outDto) {
			return outDto;
		}
		appDao.insert("App.DeviceType.addInfo", inDto);
		return DtoUtils.getSuccessDto("设备类型数据新增成功");
	}

	/**
	 * 检查设备类型编号是否存在.
	 * 
	 * @param id
	 *            编号
	 * @return 是否存在，存在返回true
	 */
	private boolean checkDeviceTypeIdExist(String id) {
		Object obj = appDao.queryForObject("App.DeviceType.getDeviceTypeById", id);
		return null != obj;
	}

	/**
	 * 检查新增信息.
	 * 
	 * @param inDto
	 *            输入信息.
	 * @return 返回信息，当检查成功返回null
	 */
	private Dto checkAddInfo(Dto inDto) {
		Dto outDto = checkInfo(inDto);
		if (null != outDto) {
			return outDto;
		}
		outDto = checkDeviceTypeIdRepeat(inDto);
		if (null != outDto) {
			return outDto;
		}
		return null;
	}

	/**
	 * 检查设备类型编号是否重复
	 * 
	 * @param inDto
	 *            dto对象
	 * @return 是否重复，不重复返回null
	 */
	private Dto checkDeviceTypeIdRepeat(Dto inDto) {
		if (checkDeviceTypeIdExist(inDto.getAsString("id"))) {
			return DtoUtils.getErrorDto("设备类型编号重复");
		}
		return null;
	}

	/**
	 * 检查输入信息.
	 * 
	 * @param inDto
	 *            输入信息.
	 * @return 返回信息，当检查成功返回空
	 */
	private Dto checkInfo(Dto inDto) {
		Dto outDto = checkIdEmpty(inDto);
		if (null != outDto) {
			return outDto;
		}

		String name = inDto.getAsString("name");
		if (AppTools.isEmptyString(name)) {
			return DtoUtils.getErrorDto("设备类型名称为空");
		}

		return null;
	}

	/**
	 * 检查
	 * 
	 * @param inDto
	 * @return
	 */
	private Dto checkIdEmpty(Dto inDto) {
		String id = inDto.getAsString("id");
		if (AppTools.isEmptyString(id)) {
			return DtoUtils.getErrorDto("设备类型编号为空");
		}
		return null;
	}

	@Override
	public Dto updateInfo(Dto inDto) {
		Dto outDto = checkInfo(inDto);
		if (null != outDto) {
			return outDto;
		}
		String oid = inDto.getAsString("oid");
		if (AppTools.isEmptyString(oid)) {
			return DtoUtils.getErrorDto("设备类型原始编号为空");
		}
		if (!checkDeviceTypeIdExist(oid)) {
			return DtoUtils.getErrorDto("原有的设备类型没有找到");
		}
		outDto = checkDeviceTypeIdRepeat(inDto);
		if (null != outDto) {
			return outDto;
		}
		appDao.update("App.DeviceType.updateInfo", inDto);
		return DtoUtils.getSuccessDto("设备类型数据更新成功");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dto deleteInfo(Dto inDto) {
		BaseRetDto outDto = new BaseRetDto();
		List<String> list = ((List<String>) inDto.getAsList("ids"));
		StringBuilder strB = new StringBuilder();
		strB.append("删除失败，编号：");
		outDto.setRetSuccess();
		for (String id : list) {
			if (0 == appDao.delete("App.DeviceType.deleteInfo", id)) {
				outDto.setRetCode(AppCommon.RET_CODE_DELETE_ERROR);
				strB.append(id);
				strB.append(", ");
			}
		}
		if (!outDto.isRetSuccess()) {
			outDto.setDesc(strB.toString());
		}
		return outDto;
	}

}
