package com.travel.role.global.s3;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.travel.role.global.exception.S3ImageNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	public static final String USER_PROFILE_IMAGE_PATH = "users/profile/";

	// Presigned url 이 유효한 시간
	private static final int PRESIGNED_URL_EXPIRE_DURATION = 1000 * 60 * 5;

	// prefix 는 버킷 내 디렉토리 이름
	// 주의!! prefix는 마지막에 / 가 붙어있어야 함 -> 위의 예시
	public String getPreSignedUrl(String prefix, String fileName) {

		if (!Objects.equals(prefix, "")) {
			fileName = prefix + fileName;
		}

		GeneratePresignedUrlRequest preSignedUrlRequest = getGeneratePreSignedUrlRequest(fileName);

		try {
			URL url = amazonS3.generatePresignedUrl(preSignedUrlRequest);
			return url.toString();
		} catch (SdkClientException e) {

			log.error("getPreSignedUrl : {}", e.getMessage());
			throw new SdkClientException("PreSigned Url 을 생성하던 중 문제가 발생하였습니다.");
		}
	}

	public String getObjectUrl(String key) {

		URL url = amazonS3.getUrl(bucket, key);

		return url.toString();
	}

	// imageProperty -> 회원 프로필 등 어떤 사진인지를 설명하는 속성
	public void checkObjectExistsOrElseThrow(String key, String imageProperty) {

		if (!amazonS3.doesObjectExist(bucket, key)) {
			throw new S3ImageNotFoundException(imageProperty);
		}
	}

	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket,
			fileName).withMethod(HttpMethod.PUT).withExpiration(getPreSignedUrlExpiration());
		generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());
		return generatePresignedUrlRequest;
	}

	private Date getPreSignedUrlExpiration() {

		return new Date(new Date().getTime() + PRESIGNED_URL_EXPIRE_DURATION);
	}
}
