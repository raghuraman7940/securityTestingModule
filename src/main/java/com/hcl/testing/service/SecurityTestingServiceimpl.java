package com.hcl.testing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zaproxy.clientapi.core.ClientApi;

import com.hcl.testing.selenium.SeleniumTestClass;
//import com.zap.client.ZapTools;

@Service
public  class SecurityTestingServiceimpl implements SecurityTestingServiceInterface {
	
	ZapTools zap = new ZapTools();

	@Override
	public void homePageActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchZap(String ZapLocation) {
		
		if( zap.startZAP(ZapLocation) == false ) {
			System.out.println( "ZAP failed to start. Terminating..." );
			System.exit(0);
		}
//		ZapStarter zs=new ZapStarter();
//		try {
//			zs.OpenZAP3();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeseleniumModule() {
		// TODO Auto-generated method stub
		try {
			SeleniumTestClass sc = new SeleniumTestClass();
			sc.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String ExecuteZap(ClientApi zapapi,String ZAP_URI_PORT,String zapoption) {
		String message=null;
		try {
			switch(zapoption) 
			{
				case "spider":
					message=zap.spider(zapapi, ZAP_URI_PORT);
					break;
				case "ascan":	
					message=zap.ascan(zapapi, ZAP_URI_PORT);
				     break;
				case "ajaxspider":
				     break;
				case "fuzzer":
				     break;
				case "selenium":
					try {
						SeleniumTestClass sc = new SeleniumTestClass();
						sc.setUp();
						message=zap.checkErrors(zapapi,ZAP_URI_PORT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				     break;
				 default:
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message=e.getMessage();
		}
		
		return message;
	}
	
}
