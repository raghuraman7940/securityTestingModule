package com.hcl.testing.controller;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zaproxy.clientapi.core.ClientApi;

import com.hcl.testing.service.SecurityTestingServiceimpl;
import com.hcl.testing.service.ZapTools;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hcl.testing.config.ZapProperties;


@RestController
@EnableConfigurationProperties(ZapProperties.class)
@RequestMapping("/api")
public class SecurityTestingController {
	
	@Autowired
	private SecurityTestingServiceimpl securityService;
	
	/**
	 * securityService securityService.
	 */
//	SecurityTestingServiceimpl securityService;
	
	/**
	 * ZAP Properties.
	 */
	private final ZapProperties properties;
	
	ClientApi zapapi=null;
	
	ZapTools zap = null;
	/**
	 * 
	 * Constructor for configuring the properties.
	 * 
	 * @param properties
	 *            properties
	 * @throws JiraException
	 *             JiraException
	 * @throws URISyntaxException 
	 */
	@Autowired
	public SecurityTestingController(ZapProperties properties) throws  URISyntaxException {
		this.properties = properties;
		zap = new ZapTools();
//		System.out.println("Properties URl:"+properties.getUrl());
//		securityService.launchZap(properties.getZapLocation());
//		 // zapapi initialize
//		zapapi = new ClientApi(properties.getzaphostname(), properties.getzapport(),properties.getzapapikey());
	}
	
	/**
	 * Execute Issue - REST API Call with JIRA.
	 * 
	 * @return String String
	 * @throws JSONException 
	 * @throws JiraException
	 *             JiraException
	 */
	@CrossOrigin
	@RequestMapping(value = "/zapconfig", method = RequestMethod.POST)
	public ResponseEntity<String> SaveZapConfig(@RequestBody String zapoptions) throws JSONException   {
		System.out.println("POST api/zapconfig  "+zapoptions);
		JSONObject jsonObject = new JSONObject(zapoptions.toString());
		JSONObject repjsonObject = new JSONObject();
		String message = null;
		try {
			
//			 // zapapi initialize
//			System.out.println("zaphostname: "+properties.getzaphostname());
//			System.out.println("zapPort: "+properties.getzapport());
//			System.out.println("zapapikey: "+properties.getzapapikey());
//			
			/* Search for issues */
			String zaphostname = jsonObject.getString("zaphostname");//"zaphostname";
			String zapport = jsonObject.getString("zapport");//"zapport";
			String zapapikey = jsonObject.getString("zapapikey");//"zapapikey";
			String zapjql = "zaphostname = " + zaphostname + " AND zapport = " + zapport + " AND zapapikey = " + zapapikey ;
		
			properties.setzaphostname(zaphostname);
			properties.setzapport( Integer.parseInt(zapport));
			properties.setzapapikey(zapapikey);
			
			String conf_zaphostname =properties.getzaphostname();
			int conf_zapport =properties.getzapport();
			String conf_zapapikey = properties.getzapapikey();
			 // zapapi initialize
			System.out.println("zaphostname: "+properties.getzaphostname());
			System.out.println("zapPort: "+properties.getzapport());
			System.out.println("zapapikey: "+properties.getzapapikey());
			
			JSONObject sprintJSON = new JSONObject();
			//jsonObject.put("id", zapOption);
			sprintJSON.put("zaphostname",conf_zaphostname);
			sprintJSON.put("zapport",conf_zapport);
			sprintJSON.put("zapapikey", conf_zapapikey);
			
			System.out.println("zapconfig Update:"+zapjql.toString());
//			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//			message=gson.toJson(message);
			repjsonObject.put("zapconfigvalues", sprintJSON);
			repjsonObject.put("message", "Zap Configured updated successfully");
			
			message = repjsonObject.toString();
			//message = "{\"message\" : \"Zap Configured successfully\" }";
		} catch (Exception e) {
			message = e.getMessage();
		}
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	

	/**
	 * Get Zap Options - REST API Call with ZAP.
	 * 
	 * @return String String
	 */
	@CrossOrigin
	@RequestMapping(value = "/launchzap", method = RequestMethod.GET)
	public ResponseEntity<String> launchzap() { 
		String message = null;
		boolean islaunchedzap;
		System.out.println("GET api/launchzap");
		try {
			
			islaunchedzap=zap.CheckIfZAPHasStartedOrNot(properties.getzaphostname(), properties.getzapport());
			//System.out.println("islaunchedzap1:"+islaunchedzap);
			if(islaunchedzap==false) {
				securityService.launchZap(properties.getZapLocation());
				islaunchedzap=zap.CheckIfZAPHasStartedOrNot(properties.getzaphostname(), properties.getzapport());
				//System.out.println("islaunchedzap1:"+islaunchedzap);
			}
//			 // zapapi initialize
//			System.out.println("zaphostname: "+properties.getzaphostname());
//			System.out.println("zapPort: "+properties.getzapport());
//			System.out.println("zapapikey: "+properties.getzapapikey());
			zapapi = new ClientApi(properties.getzaphostname(), properties.getzapport(),properties.getzapapikey());
			zap.startSession(properties.getzaphostname(), properties.getzapport(),properties.getzapapikey());
			message = "{\"message\" : \"Zap launched successfully\" }";
//			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//			message=gson.toJson(message);
		} catch (Exception e) {
			message = e.getMessage();
		}
	    return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	
	/**
	 * Get Zap Options - REST API Call with ZAP.
	 * 
	 * @return String String
	 */
	@CrossOrigin
	@RequestMapping(value = "/stopzap", method = RequestMethod.GET)
	public ResponseEntity<String> stopzap() { 
		String message = null;
		System.out.println("GET api/stopzap");
		try {
			zap.saveSession(zapapi, "Vulnerable");
			zap.stopZAP(properties.getzaphostname(), properties.getzapport(),properties.getzapapikey());
			message = "{\"message\" : \"Zap Stopped successfully\" }";
//			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//			message=gson.toJson(message);
		} catch (Exception e) {
			message = e.getMessage();
		}
	    return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	/**
	 * Get Zap Options - REST API Call with ZAP.
	 * 
	 * @return String String
	 */
	@CrossOrigin
	@RequestMapping(value = "/getzapoptions", method = RequestMethod.GET)
	public ResponseEntity<String> getProjects()   {
		String message = null;
		System.out.println("GET api/getzapoptions  ");
		try {
			System.out.println("Properties getZapOptions:"+properties.getZapOptions());
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			String propZapOptions = properties.getZapOptions();
			
			 // zapapi initialize
//				System.out.println("zaphostname: "+properties.getzaphostname());
//				System.out.println("zapPort: "+properties.getzapport());
//				System.out.println("zapapikey: "+properties.getzapapikey());
				
			List<String> lstpropZapOptions = Arrays.asList(propZapOptions.split(","));
			//System.out.println("lstpropZapOptions :"+lstpropZapOptions);
			for (String zapOption : lstpropZapOptions) {
				//System.out.println("zapOption :"+zapOption);
				JSONObject sprintJSON = new JSONObject();
				//jsonObject.put("id", zapOption);
				sprintJSON.put("name",zapOption);
				sprintJSON.put("Description","Description");
				sprintJSON.put("enabled", true);
				jsonArray.put(sprintJSON);
			}
			jsonObject.put("ZapOptions", jsonArray);
			message = jsonObject.toString();
//			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//			message=gson.toJson(message);
		} catch (Exception e) {
			message = e.getMessage();
		}
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	
	/**
	 * Execute Issue - REST API Call with JIRA.
	 * 
	 * @return String String
	 * @throws JSONException 
	 * @throws JiraException
	 *             JiraException
	 */
	@CrossOrigin
	@RequestMapping(value = "/executezapproxy", method = RequestMethod.POST)
	public ResponseEntity<String> executeIssues(@RequestBody String zapoptions) throws JSONException   {
		System.out.println("POST api/executezapproxy  "+zapoptions);
		JSONObject jsonObject = new JSONObject(zapoptions.toString());
		String message = null;
		try {
			/* Search for issues */
			String appln = jsonObject.getString("application");//"COB";
			String zapotion = jsonObject.getString("zapoption");//"Test";
			String zapjql = "Testapplication = " + appln + " AND zapotion = " + zapotion ;
		
			System.out.println("Execute Query:"+zapjql.toString());
//			DriverScript test = new DriverScript();
//			JsonObject js= test.startDriverScript(jira,jql,properties);
			message=securityService.ExecuteZap(zapapi, appln, zapotion);
			//message = "{\"message\" : \"Zap execution success\" }";
//			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//			message=gson.toJson(message);
			//message = js.toString();
		} catch (Exception e) {
			message = e.getMessage();
		}
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	
	
	@RequestMapping("/triggerSileniumDemo")
	public String triggerSileniumDemo() { 
		System.out.println("POST api/triggerSileniumDemo");
		securityService.executeseleniumModule();
	    return "seleniumTriggered";
	}
	 @RequestMapping("/home")
	public ResponseEntity dashboard() throws InterruptedException {
		 System.out.println("POST api/home");
       // dashboardService.updateClients();
        return new ResponseEntity(HttpStatus.OK);
    }
	 
		/**
		 * Login - REST API Call with ZAP.
		 * 
		 * @return String String
		 */
		@RequestMapping(value = "/login", method = RequestMethod.POST)
		public ResponseEntity<String> login(@RequestBody String login)  {
			String message = null;
			System.out.println("POST api/login  "+login);
			try {
				JSONObject jsonObject = new JSONObject(login.toString());
				String username = jsonObject.getString("username");
				String password = jsonObject.getString("password");
				System.out.println("Credentials :"+username+password);
				System.out.println("Properties URl:"+properties.getUrl());
				//jira = new JiraClient(properties.getUrl(), creds);
				message = login;
			} catch (Exception e) {
				message = e.getMessage() + ". Please check your JIRA Username and Password.";
			}

			return new ResponseEntity<>(message, HttpStatus.OK);
		}

}
