package com.example.Meeting_Notes_Summariser.jailbreaking;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class BankingTools {

    @Tool(name="get-account-balance",description = "Get the current account balance for a given account Id")
        public String getAccountBalance(@ToolParam(description = "The account is to look up") String accountId){
            if("12345".equals(accountId)){
                return "$5,000,000";
            }
            return "Account does found";
        }
}
