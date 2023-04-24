package com.mju.Board.domain.service;

import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.domain.model.Result.ListResult;
import com.mju.Board.domain.model.Result.SingleResult;
import com.mju.Board.domain.repository.FAQBoardRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {


    @AllArgsConstructor
    @Getter
    private enum CommonResponse {
        SUCCESS(200, "성공하였습니다.");

        int code;
        String message;
    }
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<T>();
        result.setData(data);
        this.setSuccessResult(result);
        return result;
    }

    public <T> ListResult<T> getListResult(List<T> list) {//sting이든 int든 다 받아서 가져와. 그걸
        //모든 타입을 다 넣을 수 있는 객체 생성
        ListResult<T> result = new ListResult<T>();
        //저장
        result.setList(list);
        //이제 commonResult타입을 받기위해 보내 그걸 다 set해줘
        this.setSuccessResult(result);
        //<T> 어떤 반환 타입이든 다 리턴 가능. 그래서 commonResult를 반환할수 있는것.
        return result;
    }
    public CommonResult getSuccessfulResult() {
        CommonResult result = new CommonResult();
        this.setSuccessResult(result);
        return result;
    }
    public CommonResult getFailResult(int code, String message) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
    }

}
