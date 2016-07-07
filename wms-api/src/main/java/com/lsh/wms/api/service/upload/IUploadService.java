package com.lsh.wms.api.service.upload;

import com.lsh.wms.model.upload.UploadForm;

/**
 * Created by xiaoma on 16/1/29.
 *
 * 上传文件
 */
public interface IUploadService {

    public String fileUpload(UploadForm uploadForm, byte[] item);

    public String urlUpload(UploadForm uploadForm);
}
