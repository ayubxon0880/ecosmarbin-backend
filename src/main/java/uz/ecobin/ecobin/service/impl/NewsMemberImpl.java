package uz.ecobin.ecobin.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.ecobin.ecobin.dto.NewsCreateDTO;
import uz.ecobin.ecobin.dto.NewsViewDTO;
import uz.ecobin.ecobin.exceptions.CommonException;
import uz.ecobin.ecobin.mapper.NewsMapper;
import uz.ecobin.ecobin.model.Image;
import uz.ecobin.ecobin.model.News;
import uz.ecobin.ecobin.repository.ImageRepository;
import uz.ecobin.ecobin.repository.NewsRepository;
import uz.ecobin.ecobin.service.NewsService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsMemberImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;


    //    https://api.telegram.org/file/bot7097780571:AAF3nCBtjDLybSDqKcxLXNVVkaQ9H-HKCr0/thumbnails/file_3.jpg
    private static final String BOT_TOKEN = "7097780571:AAF3nCBtjDLybSDqKcxLXNVVkaQ9H-HKCr0";
    private static final String RECEIVE_CHAT_ID = "5504673121";
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendDocument";
    private final ImageRepository imageRepository;
//    https://api.telegram.org/bot7097780571:AAF3nCBtjDLybSDqKcxLXNVVkaQ9H-HKCr0/getFile?file_id=AAMCAgADGQMAAgEcZpNoYYDP_-dLgeYSdbdN3Ty6ONsAAoBKAAIBWaBIt1RT3NBtnGABAAdtAAM1BA


    @Override
    public ResponseEntity<List<News>> findByLatestNews() {
        Page<News> latestNews = newsRepository.findAll(PageRequest.of(0, 3, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(latestNews.stream().toList());
    }

    @Override
    public ResponseEntity<NewsViewDTO> create(MultipartFile[] images, String newsCreateDTO) {
        Gson gson = new GsonBuilder().create();
        NewsCreateDTO newsDto = gson.fromJson(newsCreateDTO, NewsCreateDTO.class);

        List<Image> imagesModel = new ArrayList<>();
        for (MultipartFile image : images) {
            imagesModel.add(new Image(null,saveToTelegram(image)));
        }
        List<Image> images1 = imageRepository.saveAll(imagesModel);
        News news = newsMapper.toEntity(newsDto);
        news.setImages(images1);
        News save = newsRepository.save(news);
        return ResponseEntity.ok(newsMapper.toViewDto(save));
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        try {
            newsRepository.deleteById(id);
            return ResponseEntity.ok("News successfully deleted");
        } catch (Exception e){
            e.printStackTrace();
            throw new CommonException("Something is wrong. Please try again !!!",HttpStatus.CONFLICT);
        }
    }

    private String saveToTelegram(MultipartFile images) {
        HttpClient client = HttpClient.newHttpClient();
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        byte[] byteArray = null;
        try {
            byteArray = createMultipartBody(images.getBytes(), boundary, images.getOriginalFilename());
        } catch (IOException e) {
            throw new CommonException("Something is wrong please try again !", HttpStatus.CONFLICT);
        }
        String TELEGRAM_API_URL = "https://api.telegram.org/bot"+BOT_TOKEN+"/sendDocument";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TELEGRAM_API_URL))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(byteArray))
                .build();
        HttpResponse<String> response = null;
        String substring = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            substring = json.substring(json.indexOf("file_id") + 10, json.indexOf("file_unique_id") - 3);
            return extractUrl(substring);
        } catch (Exception e){
            e.printStackTrace();
            throw new CommonException("Something is wrong please try again !", HttpStatus.CONFLICT);
        }
    }

    private static byte[] createMultipartBody(byte[] fileContent, String boundary, String fileName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(("Content-Disposition: form-data; name=\"document\"; filename=\"" + fileName + "\"\r\n").getBytes());
            outputStream.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());
            outputStream.write(fileContent);
            outputStream.write(("\r\n--" + boundary + "\r\n").getBytes());
            outputStream.write(("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n").getBytes());
            outputStream.write((RECEIVE_CHAT_ID + "\r\n").getBytes());
            outputStream.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("Something is wrong please try again !", HttpStatus.CONFLICT);
        }
        return outputStream.toByteArray();
    }

    private String extractUrl(String fileId){
        String url = "https://api.telegram.org/bot"+BOT_TOKEN+"/getFile?file_id="+fileId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            String substring = json.substring(json.indexOf("file_path") + 12);

            String substring1 = substring.substring(0,substring.indexOf("\"}}"));
            return "https://api.telegram.org/file/bot"+BOT_TOKEN+"/"+substring1;
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
