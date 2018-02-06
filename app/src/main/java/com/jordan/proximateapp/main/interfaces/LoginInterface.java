package com.jordan.proximateapp.main.interfaces;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.jordan.proximateapp.main.data.ws.RequestLoginClass;
import com.jordan.proximateapp.main.data.ws.ResponseLoginClass;

/**
 * Created by jordan on 06/02/2018.
 */

public interface LoginInterface {
    @LambdaFunction
    ResponseLoginClass login(RequestLoginClass requestLoginClass);
}
