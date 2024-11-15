package com.sparta.peopleoff.common.apiresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

  private String resultCode;

  private String resultMessage;

  private String resultDescription;

  public Result(String resultCode, String resultMessage) {
    this.resultCode = resultCode;
    this.resultMessage = resultMessage;
  }

  public static Result OK() {
    return new Result(
        ResBasicCode.OK.getResCode(),
        ResBasicCode.OK.getDescription()
    );
  }

  public static Result OK(ResCodeIfs resCode) {
    return new Result(
        resCode.getHttpStatusCode().toString(),
        resCode.getResCode(),
        resCode.getDescription()
    );
  }

  public static Result ERROR(ResCodeIfs resCode) {
    return new Result(
        resCode.getHttpStatusCode().toString(),
        resCode.getResCode(),
        resCode.getDescription()
    );
  }

  public static Result ERROR(ResCodeIfs resCodeIfs, String description) {
    return new Result(
        resCodeIfs.getResCode(),
        resCodeIfs.getDescription(),
        description
    );
  }

}
