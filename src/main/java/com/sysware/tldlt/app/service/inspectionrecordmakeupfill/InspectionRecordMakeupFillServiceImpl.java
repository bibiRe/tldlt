package com.sysware.tldlt.app.service.inspectionrecordmakeupfill;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;

import java.util.List;

import org.g4studio.common.service.impl.BaseServiceImpl;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.common.dao.vo.UserInfoVo;
import org.g4studio.system.common.util.SystemConstants;
import org.g4studio.system.common.util.idgenerator.IdGenerator;
import org.springframework.dao.DataAccessException;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

import org.g4studio.system.common.util.idgenerator.IDHelper;


public class InspectionRecordMakeupFillServiceImpl      extends BaseAppServiceImpl implements InspectionRecordMakeupFillService
{
	public String getRecordMakeup(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecordMakeupFill.getRecordMakeupCount", pDto);
			List items = appDao.queryForPage("App.InspectRecordMakeupFill.getRecordMakeup", pDto);
			
			
			for(int i = 0;i < items.size();i++)
			{
				Dto item = (BaseDto)items.get(i);
				String inspecplanid = item.getAsString("inspecplanid");
				
				Dto inDto = new BaseDto();
				
				inDto.put("inspecplanid", inspecplanid);

				boolean initcheck = false;
				
				List items1 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", inDto);
				if(items1.size() > 0)
				{
					Dto item1 = (BaseDto)items1.get(0);
					
					String recordexecuteendtime = item1.getAsString("recordexecuteendtime");
					if(recordexecuteendtime != null && recordexecuteendtime.length() > 0)
					{
						initcheck = true;
					}
				}
				
				item.put("initialcheck", new Boolean(initcheck));
			}
					

			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	
	public String getInspecPlanDevice(Dto pDto)
	{
		String str = "";
		
		try
		{

			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecordMakeupFill.getInspecPlanDeviceCount", pDto);
			List items = appDao.queryForPage("App.InspectRecordMakeupFill.getInspecPlanDevice", pDto);

			
			if(items.size() > 0)
			{
				
				Dto item = new BaseDto();
				for (int i = 0; i < items.size(); i++) 
				{
					item = (BaseDto) items.get(i);
					
					item.put("initialcheck", new Boolean(false));
					
					String planid = item.getAsString("planid");
					String plandevid = item.getAsString("plandevid");
					
					
					Dto inDto2 = new BaseDto();
					
					inDto2.put("inspecplanid", planid);
					
					
					List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", inDto2);

					if(items2.size() > 0)
					{
						Dto item2 = (BaseDto) items2.get(0);
						
						String recordid = item2.getAsString("recordid");
						
						
						Dto inDto3 = new BaseDto();
						
						inDto3.put("recordid", recordid);
						inDto3.put("plandevid", plandevid);
						
						List items3 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", inDto3);
						
						if(items3.size() > 0)
						{
							item.put("initialcheck", new Boolean(true));
						}
						
					}

				}
				
			}
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getInspectRecordInfo(Dto pDto)
	{
		String str = "查询为空";
		
		try
		{

			
					String planid = pDto.getAsString("planid");
					String plandevid = pDto.getAsString("plandevid");
					
					
					Dto inDto = new BaseDto();
					
					inDto.put("inspecplanid", planid);
					
					
					List items = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", inDto);

					if(items.size() > 0)
					{
						Dto item = (BaseDto) items.get(0);
						
						String recordid = item.getAsString("recordid");
						
						
						Dto inDto2 = new BaseDto();
						
						inDto2.put("recordid", recordid);
						inDto2.put("plandevid", plandevid);
						
						List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", inDto2);
						
						str = JsonHelper.encodeList2PageJson(items2,items2.size(),null);

						
					}
			
		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	public String getInspectRecordInfo2(Dto pDto)
	{
		String str = "";
		
		try
		{

			
					String planid = pDto.getAsString("inspecplanid");
					String plandevid = pDto.getAsString("plandevid");
					
					
					Dto inDto = new BaseDto();
					
					inDto.put("inspecplanid", planid);
					
					
					List items = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", inDto);

					if(items.size() > 0)
					{
						Dto item = (BaseDto) items.get(0);
						
						String recordid = item.getAsString("recordid");
						
						
						Dto inDto2 = new BaseDto();
						
						inDto2.put("recordid", recordid);
						inDto2.put("plandevid", plandevid);
						
						List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", inDto2);
						
						//str = JsonHelper.encodeList2PageJson(items2,items2.size(),null);

						if(items2.size() > 0)
						{
							Dto item2 = (BaseDto) items2.get(0);
							
							str = item2.getAsString("recordinfoid");
						}
						
					}
			
		}
		catch(Exception e)
		{
			str = "";
		}
		
		
		return str;
	}
	
	private String getInspecRecordId(Dto pDto)
	{
		
		String recordid = "";
		
		try
		{

			
			
			List items = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", pDto);

			if(items.size() > 0)
			{
				Dto item = (BaseDto) items.get(0);
				
				recordid = item.getAsString("recordid");
			}
			else
			{
				    Dto inDto = new BaseDto();
				 
		            String newID = IDHelper.NewInspectRecordID();
		        	
		            inDto.put("id", newID);
		            inDto.put("inspecplanid", pDto.getAsString("inspecplanid"));
		      
			        try
			        {
			
			        	appDao.insert("App.InspectRecordMakeupFill.AddInspecRecord", inDto);
			        	
			        	recordid = newID;
			        	
			        	
			        	
						 pDto.put("recordid", recordid);
						 pDto.put("recordmakeupstate", "5"); 
						 pDto.put("inspecplanstate", "5"); 
						
						 appDao.update("App.InspectRecordMakeup.UpdateRecordMakeupState", pDto);  
						 appDao.update("App.InspectPlan.UpdatePlanState", pDto); 
			        }
			        
			        catch(Exception e)
			        {
			        	
			        	
			        }
			}

		}
		catch(Exception e)
		{
	
		}
		
		return recordid;
	}
	
	
	public Dto finishInspecRecord(Dto pDto)
	{
		
		String str = "";
		
		try
		{

			List items1 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", pDto);
			if(items1.size() <= 0)
			{
				return DtoUtils.getErrorDto("操作失败，巡检没有完成，不能关闭!");
			}
			
			Dto item1 = (BaseDto)items1.get(0);
			
			String recordexecuteendtime = item1.getAsString("recordexecuteendtime");
			if(recordexecuteendtime != null && recordexecuteendtime.length() > 0)
			{
				return DtoUtils.getErrorDto("操作失败，巡检已经关闭!");
			}
			
			List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecPlanDevice", pDto);
			
			String recordid = item1.getAsString("recordid");
			
			Dto inDto = new BaseDto();
			
			inDto.put("recordid", recordid);
			
			List items3 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", inDto);
			
			if(items2.size() != items3.size())
			{
				return DtoUtils.getErrorDto("操作失败，巡检没有完成，不能关闭!");
			}
			
			for(int i = 0;i < items2.size();i++)
			{
				boolean found = false;
				Dto item2 = (BaseDto)items2.get(i);
				
				String plandevid2 = item2.getAsString("plandevid");
				
				for(int j = 0;j < items3.size();j++)
				{
					Dto item3 = (BaseDto)items3.get(j);
					String plandevid3 = item3.getAsString("plandevid");
					
					if(plandevid2 != null && plandevid3 != null)
					{
						if(plandevid2.equals(plandevid3))
						{
							found = true;
						}
					}
				}
				
				if(!found)
				{
					return DtoUtils.getErrorDto("操作失败，巡检没有完成，不能关闭!");
				}
			}
			
			
			 pDto.put("recordid", recordid);
			 pDto.put("recordmakeupstate", "6"); 
			 pDto.put("inspecplanstate", "6"); 
			
			 appDao.update("App.InspectRecordMakeupFill.UpdateInspecRecord", pDto);  
			 appDao.update("App.InspectRecordMakeup.UpdateRecordMakeupState", pDto);  
			 appDao.update("App.InspectPlan.UpdatePlanState", pDto);  
			
			
		}
		catch(Exception e)
		{
			
		}
		
		return DtoUtils.getSuccessDto("操作成功");
	}
	
	private boolean isRecordInfoReady(Dto pDto)
	{
		boolean ok = false;
		
		try
		{

			List items = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", pDto);
			
			if(items.size() > 0)
			{
			
				ok = true;
			}
		
		}
		
		catch(Exception e)
		{
			
		}
		
		return ok;
	}
	
	public Dto addInspecPlanRecordInfo(Dto pDto)
	{
		
		try
		{

					String recordid = getInspecRecordId(pDto);
					
					 pDto.put("recordid", recordid);
					
					if(isRecordInfoReady(pDto))
					{
						 appDao.update("App.InspectRecordMakeupFill.UpdateInspecRecordInfo", pDto);  
					}
			
					else
					{
					   
			            String newID = IDHelper.NewInspectRecordInfoID();
			        	
			            pDto.put("id", newID);

				        appDao.insert("App.InspectRecordMakeupFill.AddInspecRecordInfo", pDto);  
					}
			        
			        
			        //if(all dev settting)
			      //  {
			        	//finishInspecRecord(pDto);
			       // }

		}
		
		catch(Exception e)
		{
			 return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
		}
		
		return DtoUtils.getSuccessDto("操作成功");
	}
	
	
	
	public String getDeviceCheckContentInfo(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecordMakeupFill.getDeviceCheckContentCount", pDto);
			List items = appDao.queryForPage("App.InspectRecordMakeupFill.getDeviceCheckContent", pDto);
			
			
			if(items.size() > 0)
			{
				
				Dto item = new BaseDto();
				for (int i = 0; i < items.size(); i++) 
				{
					item = (BaseDto) items.get(i);
					
					item.put("initialcheck", new Boolean(false));
					
					String planid = item.getAsString("planid");
					String plandevid = item.getAsString("plandevid");
					String devcheckcontentid = item.getAsString("devcheckcontentid");
					
					
					Dto inDto2 = new BaseDto();
					
					inDto2.put("inspecplanid", planid);
					
					
					List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecord", inDto2);

					if(items2.size() > 0)
					{
						Dto item2 = (BaseDto) items2.get(0);
						
						String recordid = item2.getAsString("recordid");
						
						
						Dto inDto3 = new BaseDto();
						
						inDto3.put("recordid", recordid);
						inDto3.put("plandevid", plandevid);
						
						List items3 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", inDto3);
						
						if(items3.size() > 0)
						{
							Dto item3 = (BaseDto) items3.get(0);
							String recordinfoid = item3.getAsString("recordinfoid");
							
							Dto inDto4 = new BaseDto();
							
							inDto4.put("recordinfoid", recordinfoid);
							inDto4.put("devcheckcontentid", devcheckcontentid);
							
							
							List items4 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordItem", inDto4);
							if(items4.size() > 0)
							{
								item.put("initialcheck", new Boolean(true));
							}
							
							
						}
						
					}

				}
			}
			
			
			

			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	private boolean isRecordItemReady(Dto pDto)
	{
		boolean ok = false;
		
		try
		{

			List items = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", pDto);
			
			if(items.size() > 0)
			{

				 Dto item = (BaseDto) items.get(0);
				 String recordinfoid = item.getAsString("recordinfoid");
				
				 pDto.put("recordinfoid", recordinfoid);
				 
				 
				 List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordItem", pDto);
				 if(items2.size() > 0)
				 {
					 ok = true;
				 }
				
			}
		
		}
		
		catch(Exception e)
		{
			
		}
		
		return ok;
	}
	
	public Dto addInspecPlanRecordItem(Dto pDto)
	{
		
		try
		{

					String recordid = getInspecRecordId(pDto);
					
					 pDto.put("recordid", recordid);
					
					if(isRecordItemReady(pDto))
					{
						 appDao.update("App.InspectRecordMakeupFill.UpdateInspecRecordItem", pDto);  
					}
			
					else
					{
					   
			            String newID = IDHelper.NewInspectRecordItemID();
			        	
			            pDto.put("id", newID);

				        appDao.insert("App.InspectRecordMakeupFill.AddInspecRecordItem", pDto);  
					}
			       

		}
		
		catch(Exception e)
		{
			 return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
		}
		
		return DtoUtils.getSuccessDto("操作成功");
	}
	
	
	
	public String getInspectRecordItem(Dto pDto)
	{
		String str = "查询为空";
		
		try
		{
			
					String recordid = getInspecRecordId(pDto);
			
					pDto.put("recordid", recordid);
					
					
					List items = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordInfo", pDto);
					
					if(items.size() > 0)
					{

						 Dto item = (BaseDto) items.get(0);
						 String recordinfoid = item.getAsString("recordinfoid");
						
						 pDto.put("recordinfoid", recordinfoid);
						 
						 
						 List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordItem", pDto);
		
						 
						 str = JsonHelper.encodeList2PageJson(items2,items2.size(),null);
						
					}	
			
		}
		
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	public String getInspectRecordItem2(Dto pDto)
	{
		String str = "";
		
		try
		{
			
             String recordinfoid = getInspectRecordInfo2(pDto);
			
			 pDto.put("recordinfoid", recordinfoid);
			 
			 
			 List items2 = appDao.queryForList("App.InspectRecordMakeupFill.getInspecRecordItem", pDto);

			 
			if(items2.size() > 0)
			{
				Dto item2 = (BaseDto) items2.get(0);
					
				str = item2.getAsString("recorditemid");
			}
						
					
			
		}
		
		catch(Exception e)
		{
			str = "";
		}
		
		
		return str;
	}
	
	
}
