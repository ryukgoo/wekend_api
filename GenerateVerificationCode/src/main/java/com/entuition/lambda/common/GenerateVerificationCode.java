package com.entuition.lambda.common;

import java.util.Random;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.surem.api.sms.SureSMSAPI;
import com.surem.net.SendReport;
import com.surem.net.sms.SureSMSDeliveryReport;

public class GenerateVerificationCode implements RequestHandler<VerificationRequest, VerificationResponse> {

	private LambdaLogger logger;
	
	public VerificationResponse handleRequest(VerificationRequest input, Context context) {
		
		logger = context.getLogger();
		
		String phoneNumber = input.getPhone();
		String verificationCode = String.format("%06d", new Random().nextInt(1000000));
		
		int member = 100;
        String usercode = "picnic";
        String username = "guest";
        String callphone1 = phoneNumber.substring(0, 3);
        String callphone2 = phoneNumber.substring(3, 7);
        String callphone3 = phoneNumber.substring(7, 11);

        final String callmessage = "[Wekend] 인증번호는 " + verificationCode + " 입니다. 정확히 입력해 주세요.";
        String rdate = "00000000";
        String rtime = "000000";
        String reqphone1 = "070";
        String reqphone2 = "7565";
        String reqphone3 = "4702";
        String callname = "인증번호발신";
        String deptcode = "GC-OIJ-B5";
		
		SureSMSAPI sms = new SureSMSAPI() {

			@Override
			public void report(SureSMSDeliveryReport deliveryReport) {
				logger.log("msgKey = " + deliveryReport.getMember());
				logger.log("result = " + deliveryReport.getResult());
				logger.log("errorCode = " + deliveryReport.getErrorCode());
				logger.log("recvTime = " + deliveryReport.getRecvDate() + deliveryReport.getRecvTime());
			}
		};
		
		SendReport sendReport = sms.sendMain(member, usercode, deptcode, username,
				callphone1, callphone2, callphone3, callname, reqphone1, reqphone2, reqphone3, callmessage, rdate, rtime);
		
		String result = sendReport.getStatus();
		
		logger.log("result : " + result);
		
		return new VerificationResponse(verificationCode);
		
//		if (result.trim().equals("O")) {
//			return new VerificationResponse(verificationCode);			
//		}
//		
//		return null;
	}
	
}
