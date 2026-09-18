package com.example.Meeting_Notes_Summariser.dto;

import java.util.List;

public record SummarizationResponse(
        List<String> actionItems,
        List<String> decisions,
        String errormessage
){
}
