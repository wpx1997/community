package life.wpx1997.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.*;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.security.pkcs11.wrapper.Constants;

import java.io.InputStream;
import java.util.UUID;

/**
 * created by 小case on 2019/8/27 21:52
 * @author 不会飞的小鹏
 */
@Service
public class UCloudProvider {

    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    @Value("${ucloud.ufile.expires}")
    private Integer expires;

    public String upload(InputStream fileStream,String mimeType,String fileName){

        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length>1){
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        }else {
            return null;
        }

        try {
            ObjectAuthorization objectAuthorization  = new UfileObjectLocalAuthorization(publicKey, privateKey);
            ObjectConfig config = new ObjectConfig(region, suffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization , config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
            if (response != null && response.getRetCode() == 0){
                String url = UfileClient.object(objectAuthorization,config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName,expires)
                        .createUrl();
                return url;
            }else {
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }

        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
