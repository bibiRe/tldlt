package com.sysware.tldlt.app.service.region;

import java.util.Collection;
import java.util.List;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.system.admin.service.OrganizationService;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：RegionServiceImpl
 * Descript：区域服务实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月13日 下午1:53:19
 * Version：@version
 */
public class RegionServiceImpl extends BaseAppServiceImpl implements
        RegionService {
    /**
     * 组织服务接口.
     */
    private OrganizationService organizationService;

    @Override
    public Dto addInfo(Dto inDto) {
        Dto outDto = checkAddInfo(inDto);
        if (null != outDto) {
            return outDto;
        }
        appDao.insert("App.Region.addInfo", inDto);
        return DtoUtils.getSuccessRetDto("区域数据新增成功");
    }

    /**
     * 检查新增信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回null
     */
    private Dto checkAddInfo(Dto inDto) {
    	Integer regionId = inDto.getAsInteger("regionid");
		if ((null != regionId) && (0 != regionId.intValue()) ) {
			return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
					"记录编号不为空");
		}
        Dto outDto = checkInfo(inDto);
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
    @SuppressWarnings("unchecked")
    private Dto checkInfo(Dto inDto) {
        String name = inDto.getAsString("regionname");
        if (AppTools.isEmptyString(name)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "区域名称为空");
        }
        String departmentid = inDto.getAsString("departmentid");
        if (AppTools.isEmptyString(departmentid)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "所属部门为空");
        }
        Dto queryDto = new BaseDto();
        queryDto.put("deptid", departmentid);
        Dto outDto = organizationService.queryDeptinfoByDeptid(queryDto);
        if (null == outDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "没有此部门");
        }
        Integer pid = inDto.getAsInteger("parentid");
        if ((null != pid) && (0 != pid.intValue())) {
            if (null == appDao.queryForObject("App.Region.queryRegionInfoById", pid.intValue())) {
                return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                        "上级区域无效");
            }
        } else {
        	inDto.put("parentid", null);
        }
        
        outDto = checkRegionType(inDto);
        if (null != outDto) {
        	return outDto;
        }
        
        return null;
    }

	/**
	 * 检查区域类型.
	 * @param inDto 输入dto
	 * @return dto.
	 */
	private Dto checkRegionType(Dto inDto) {
		Integer regionType = inDto.getAsInteger("regiontype");
        if (null == regionType) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "区域类型不存在");
        }
        if (!DtoUtils.checkDictCodeExist("REGIONTYPE", regionType.toString())) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "区域类型无效");
        }
        return null;
	}
	
	/**
	 * 检查更新输入信息.
     * @param inDto 输入信息.
     * @return 返回信息，当检查成功返回空
	 */
	private Dto checkUpdateInfo(Dto inDto) {
		Integer regionId = inDto.getAsInteger("regionid");
		if ((null == regionId) || (0 == regionId.intValue()) ) {
			return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
					"记录编号为空");
		}
		Dto outDto = checkInfo(inDto);
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
	@SuppressWarnings("unchecked")
	private void deleteId(int id, BaseRetDto outDto) {
		List<Integer> list = (List<Integer>) appDao.queryForList("App.Region.queryChildRegionIds", id);
		for(Integer ids: list) {
			deleteId(ids, outDto);
		}
		appDao.delete("App.Region.deleteInfo", id);
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

    @SuppressWarnings("rawtypes")
    @Override
    public Collection queryRegionItems(Dto dto) {
        List result = appDao.queryForList("App.Region.queryRegionItemsByDto",
                dto);
        return result;
    }

    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

	@Override
    public Dto updateInfo(Dto inDto) {
    	Dto outDto = checkUpdateInfo(inDto);        
    	if (null != outDto) {
    		return outDto;
    	}
        appDao.update("App.Region.updateInfo", inDto);
        return DtoUtils.getSuccessRetDto("区域数据更新成功");
    }

}
