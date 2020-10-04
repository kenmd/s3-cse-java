package org.example.basicapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class S3cseTest {

    @Mock
    S3CSEClient client;
    @Captor
    ArgumentCaptor<String> s3uriCap;
    @Captor
    ArgumentCaptor<File> fileCap;

    @Mock
    File mockedFile;

    S3cse s3cse;

    @BeforeEach
    void init() {
        s3cse = new S3cse(client);

        // NOTE: boolean mock methods return false by mockito default
        // for example, client.exists() mockedFile.isDirectory()
    }

    @Test
    public void testCLIUpload() {
        // local file always exist so that user can always upload the mock file
        when(mockedFile.getName()).thenReturn("hoge.txt");
        when(mockedFile.exists()).thenReturn(true);

        try {
            s3cse.upload(mockedFile, "s3://bucket/");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        verify(client, times(1)).putObject(s3uriCap.capture(), fileCap.capture());

        assertEquals("hoge.txt", fileCap.getValue().getName());
        assertEquals("s3://bucket/hoge.txt", s3uriCap.getValue());
    }

    @Test
    public void testUploadDirctory() {
        // assume mockedFile is directory and always exist
        when(mockedFile.getName()).thenReturn("hoge.txt");
        when(mockedFile.exists()).thenReturn(true);
        when(mockedFile.isDirectory()).thenReturn(true);

        Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
            s3cse.upload(mockedFile, "s3://bucket/");
        });

        assertEquals("Recursive upload is not implemented", exception.getMessage());

        verify(client, times(0)).putObject(s3uriCap.capture(), fileCap.capture());
    }

    @Test
    public void testCLIDownload() {
        // local file always does not exist so that we can skip to ask user
        when(mockedFile.getName()).thenReturn("hoge.txt");

        s3cse.download(mockedFile, "s3://bucket/hoge.txt");

        verify(client, times(1)).getObject(s3uriCap.capture(), fileCap.capture());

        assertEquals("hoge.txt", fileCap.getValue().getName());
        assertEquals("s3://bucket/hoge.txt", s3uriCap.getValue());
    }

    @Test
    public void testDownloadFromDirectory() {
        Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
            s3cse.download(mockedFile, "s3://bucket/");
        });

        assertEquals("Recursive download is not implemented", exception.getMessage());

        verify(client, times(0)).getObject(s3uriCap.capture(), fileCap.capture());
    }

    @Test
    public void testDownloadToDirectory() {
        // assume mockedFile is directory
        when(mockedFile.getName()).thenReturn(".");
        when(mockedFile.isDirectory()).thenReturn(true);
        when(client.getBasename("s3://bucket/hoge.txt")).thenReturn("hoge.txt");

        s3cse.download(mockedFile, "s3://bucket/hoge.txt");

        verify(client, times(1)).getObject(s3uriCap.capture(), fileCap.capture());

        assertEquals("hoge.txt", fileCap.getValue().getName());
        assertEquals("s3://bucket/hoge.txt", s3uriCap.getValue());
    }
}
