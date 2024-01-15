package com.buyersfirst.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

  @Value("${root.path}")
  private String rootPath;

  @PostConstruct
  public void init() {
    try {
      System.out.println(rootPath);
      Files.createDirectories(Paths.get(rootPath));
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  public void save(MultipartFile file, String path, boolean update) throws Exception{
    try {
      Path dest = Paths.get(path);
      Path relativePath = Files.createDirectories(Paths.get(Paths.get(rootPath).toString(), dest.getParent().toString()));
      Path destFilePath = Paths.get(relativePath.toString(), dest.getFileName().toString());
      System.out.println(destFilePath.toString());
      if (!update)
        Files.copy(file.getInputStream(), destFilePath);
      else
        Files.copy(file.getInputStream(), destFilePath, StandardCopyOption.REPLACE_EXISTING);

    } catch (Exception e) {
      throw e;
    }
  }

  public Resource load(String filename) throws Exception{
    try {
      Path requestFilePath = Paths.get(filename);
      Path file = Paths.get(Paths.get(rootPath).toString(), requestFilePath.toString());
      System.out.println(file.toString());
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
}
