package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.ImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final ImageRepository imageRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;
    private final UserImageRepository userImageRepository;
    private final EventImageRepository eventImageRepository;

    @Transactional
    public Boolean readyForUpload(Object object, MultipartFile image) {
        if (image.isEmpty()) {
            throw new CommonException(ErrorCode.INVALID_IMAGE);
        }

        Optional<Image> optionalOldImage = getOldImage(object);

        if (optionalOldImage.isPresent()) {
            Image oldImage = optionalOldImage.get();
            Long imageId = oldImage.getId();

            String imageName = image.getOriginalFilename().split("\\.")[0] + getInfoString(object);

            if (oldImage.getName().equals(imageName)) {
                log.info("기존 이미지와 동일 -> 변경 필요 없음");
                return Boolean.FALSE;
            }

            deleteOldImage(object, imageId, imageName);
            log.info("기존 이미지 삭제 완료");

            return Boolean.TRUE;
        }
        return Boolean.TRUE;
    }

    private Optional<Image> getOldImage(Object object) {
        if (object instanceof User) {
            return userImageRepository.findByUser((User) object);
        } else if (object instanceof Event) {
            return eventImageRepository.findByEvent((Event) object);
        }
        throw new CommonException(ErrorCode.NOT_FOUND_IMAGE);
    }

    private String getInfoString(Object object) {
        if (object instanceof User) {
            return ((User) object).getSerialId();
        } else if (object instanceof Event) {
            return ((Event) object).getName();
        }
        throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void deleteOldImage(Object object, Long imageId, String imageName) {
        amazonS3Client.deleteObject(bucketName, imageName);
        log.info("S3 이미지 삭제 완료");

        if (object instanceof User) {
            userImageRepository.deleteById(imageId);
            log.info("UserImage 삭제 완료");
        } else if (object instanceof Event) {
            eventImageRepository.deleteById(imageId);
            log.info("EventImage 삭제 완료");
        }

        imageRepository.deleteById(imageId);
        log.info("Image 삭제 완료");
    }


    public String uploadToS3(Object object, MultipartFile image) throws IOException {
        // 확장자
        String contentType = image.getOriginalFilename().split("\\.")[1];

        ObjectMetadata metadata = new ObjectMetadata();
        switch (contentType){
            case "jpg", "jpeg":
                metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                metadata.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
            default:
                throw new CommonException(ErrorCode.INVALID_IMAGE_EXTENSION);
        }

        // 이미지 이름
        String fileName;
        if (object instanceof User){
            User user = (User) object;
            fileName = image.getOriginalFilename().split("\\.")[0] + user.getSerialId();
        }
        else {
            Event event = (Event) object;
            fileName = image.getOriginalFilename().split("\\.")[0] + event.getName();
        }

        amazonS3Client.putObject(bucketName, fileName, image.getInputStream(), metadata);
        log.info("s3 전달 성공");

        return amazonS3Client.getResourceUrl(bucketName, fileName);
    }
}
