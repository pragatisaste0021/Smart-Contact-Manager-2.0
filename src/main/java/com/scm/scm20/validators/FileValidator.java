package com.scm.scm20.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile>{

    @Value("${MAX_FILE_SIZE:10MB}")
    private DataSize MAX_FILE_SIZE;


    // private static final long MAX_FILE_SIZE = 1024*1024*2;   //2mb

    // type
    // height
    // width

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        
        if(file == null || file.isEmpty()){
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("File can not be empty").addConstraintViolation();
            return true;
        }
        
        // File Size
        if(file.getSize() > MAX_FILE_SIZE.toBytes()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should not be more than 2 MB").addConstraintViolation();
            return false;
        }

        // // Resolution
        // try {
        //     BufferedImage bufferedImage =  ImageIO.read(file.getInputStream());
        //     if(bufferedImage.getWidth() > 200){

        //     }
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        return true;
    }

}
