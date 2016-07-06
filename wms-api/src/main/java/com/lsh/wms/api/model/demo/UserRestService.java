package com.lsh.wms.api.model.demo;

/**
 * Created by fuhao on 16/1/20.
 */
import javax.validation.constraints.Min;

public interface UserRestService {
    User getUser(@Min(value = 1L, message = "User ID must be greater than 1") Long id);
}
