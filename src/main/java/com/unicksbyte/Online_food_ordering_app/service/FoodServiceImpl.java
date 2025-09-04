package com.unicksbyte.Online_food_ordering_app.service;

import com.unicksbyte.Online_food_ordering_app.entity.FoodEntity;
import com.unicksbyte.Online_food_ordering_app.io.FoodRequest;
import com.unicksbyte.Online_food_ordering_app.io.FoodResponse;
import com.unicksbyte.Online_food_ordering_app.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService{

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public FoodResponse updateFood(String id, FoodRequest request, MultipartFile file) {
        FoodEntity existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found with id: " + id));

        existingFood.setName(request.getName());
        existingFood.setDescription(request.getDescription());
        existingFood.setPrice(request.getPrice());
        existingFood.setCategory(request.getCategory());

        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadFile(file);
            existingFood.setImageUrl(imageUrl);
        }

        FoodEntity updatedFood = foodRepository.save(existingFood);
        return convertToResponse(updatedFood);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String fileNameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String key = UUID.randomUUID().toString()+"."+fileNameExtension;

        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if (response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }
            else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File upload failed..!");

            }



        }
            catch(IOException ex){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occured during uploading the file");

        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {


        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity = foodRepository.save(newFoodEntity);
        return convertToResponse(newFoodEntity);
    }


    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> foodEntities = foodRepository.findAll();
        return  foodEntities.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());

    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity existingFoodEntity = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("food not found at the id:" + id));
        return convertToResponse(existingFoodEntity);

    }

    @Override
    public boolean deleteFile(String file) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(file)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
        String imageUrl = response.getImageUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        boolean isFileDeleted = deleteFile(fileName);
        if(isFileDeleted)
        {
            foodRepository.deleteById(response.getId());
        }
    }

    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity)
    {
        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
