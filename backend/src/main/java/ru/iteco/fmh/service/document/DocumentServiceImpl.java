package ru.iteco.fmh.service.document;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.iteco.fmh.dao.repository.DocumentRepository;
import ru.iteco.fmh.dao.repository.UserRepository;
import ru.iteco.fmh.dto.document.DocumentCreationDtoRq;
import ru.iteco.fmh.dto.document.DocumentCreationDtoRs;
import ru.iteco.fmh.model.document.Document;
import ru.iteco.fmh.model.user.User;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    @Override
    public DocumentCreationDtoRs createDocument(DocumentCreationDtoRq documentCreationDtoRq) {
        Document document = conversionService.convert(documentCreationDtoRq, Document.class);
        String currentUserLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByLogin(currentUserLogin);
        document.setCreateDate(Instant.now());
        document.setUser(user);
        document = documentRepository.save(document);
        return conversionService.convert(document, DocumentCreationDtoRs.class);
    }
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.fmh.Util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String uploadDocument(MultipartFile multipartFile) {
        final String urlSeparator = "/";

        String md5FileName = Util.getMd5NameFromDocumentName(multipartFile.getOriginalFilename());
        File pathToUploadDocument = new File(uploadPath);
        pathToUploadDocument.mkdirs();

        try {
            multipartFile.transferTo(Path.of(uploadPath + md5FileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return urlSeparator + pathToUploadDocument.getName() + urlSeparator + md5FileName;
    }
}
