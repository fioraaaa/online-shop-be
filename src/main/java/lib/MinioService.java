package lib;
import lib.exception.CustomMinioException;
import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    public List<Bucket> listBuckets() throws MinioException {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new CustomMinioException("Error occurred while listing buckets: " + e.getMessage(), e);
        }
    }

    public void uploadFile(String bucketName, String objectName, String filePath) throws MinioException {
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build());
        } catch (Exception e) {
            throw new CustomMinioException("Error occurred while uploading file: " + e.getMessage(), e);
        }
    }

    public void downloadFile(String bucketName, String objectName, String filePath) throws CustomMinioException {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build());
        } catch (Exception e) {
            throw new CustomMinioException("Error occurred while downloading file: " + e.getMessage(), e);
        }
    }
}
