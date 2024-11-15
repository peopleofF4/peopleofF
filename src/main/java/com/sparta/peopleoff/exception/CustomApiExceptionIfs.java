package com.sparta.peopleoff.exception;

import com.sparta.peopleoff.common.rescode.ResCodeIfs;

public interface CustomApiExceptionIfs {

  ResCodeIfs getErrorCode();

  String getErrorDescription();

}
