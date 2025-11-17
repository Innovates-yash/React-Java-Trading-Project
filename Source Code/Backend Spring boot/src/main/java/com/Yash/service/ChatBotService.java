package com.Yash.service;

import com.Yash.model.CoinDTO;
import com.Yash.response.ApiResponse;

public interface ChatBotService {
    ApiResponse getCoinDetails(String coinName);

    CoinDTO getCoinByName(String coinName);

    String simpleChat(String prompt);
}
