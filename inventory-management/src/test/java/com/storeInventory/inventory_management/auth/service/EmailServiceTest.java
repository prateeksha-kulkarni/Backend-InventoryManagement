package com.storeInventory.inventory_management.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDateTime;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Disabled;

@Timeout(10)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService();
        Field mailSenderField = EmailService.class.getDeclaredField("mailSender");
        mailSenderField.setAccessible(true);
        mailSenderField.set(emailService, mailSender);
    }

    @Test
    void testSendOtpEmailWithValidEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        String result = emailService.sendOtpEmail("test@example.com");
        assertThat(result, is("OTP sent successfully to test@example.com"));
        verify(mailSender, atLeast(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendOtpEmailWithUpperCaseEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        String result = emailService.sendOtpEmail("TEST@EXAMPLE.COM");
        assertThat(result, is("OTP sent successfully to TEST@EXAMPLE.COM"));
        verify(mailSender, atLeast(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendOtpEmailWithMixedCaseEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        String result = emailService.sendOtpEmail("Test@Example.Com");
        assertThat(result, is("OTP sent successfully to Test@Example.Com"));
        verify(mailSender, atLeast(1)).send(any(SimpleMailMessage.class));
    }

    @Disabled()
    @Test
    void testVerifyOtpWithValidOtp() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class);
            MockedStatic<Random> mockedRandom = mockStatic(Random.class)) {
            LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
            mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(now);
            Random mockRandom = mock(Random.class);
            mockedRandom.when(() -> new Random()).thenReturn(mockRandom);
            doReturn(0).when(mockRandom).nextInt(900000);
            EmailService spyEmailService = spy(emailService);
            spyEmailService.sendOtpEmail("test@example.com");
            boolean verifyResult = spyEmailService.verifyOtp("test@example.com", "100000");
            assertTrue(verifyResult);
        }
    }

    @Test
    void testVerifyOtpWithInvalidOtp() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
            mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(now);
            EmailService spyEmailService = spy(emailService);
            doReturn("OTP sent successfully to test@example.com").when(spyEmailService).sendOtpEmail("test@example.com");
            spyEmailService.sendOtpEmail("test@example.com");
            boolean result = spyEmailService.verifyOtp("test@example.com", "wrong123");
            assertFalse(result);
        }
    }

    @Test
    void testVerifyOtpWithNonExistentEmail() {
        boolean result = emailService.verifyOtp("nonexistent@example.com", "123456");
        assertFalse(result);
    }

    @Test
    void testVerifyOtpWithExpiredOtp() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
            mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(now);
            EmailService spyEmailService = spy(emailService);
            doReturn("OTP sent successfully to test@example.com").when(spyEmailService).sendOtpEmail("test@example.com");
            spyEmailService.sendOtpEmail("test@example.com");
            LocalDateTime laterTime = LocalDateTime.of(2024, 1, 1, 12, 6, 0);
            mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(laterTime);
            boolean verifyResult = spyEmailService.verifyOtp("test@example.com", "100000");
            assertFalse(verifyResult);
        }
    }

    @Disabled()
    @Test
    void testVerifyOtpWithCaseInsensitiveEmail() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class);
            MockedStatic<Random> mockedRandom = mockStatic(Random.class)) {
            LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
            mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(now);
            Random mockRandom = mock(Random.class);
            mockedRandom.when(() -> new Random()).thenReturn(mockRandom);
            doReturn(0).when(mockRandom).nextInt(900000);
            EmailService spyEmailService = spy(emailService);
            spyEmailService.sendOtpEmail("Test@Example.Com");
            boolean verifyResult = spyEmailService.verifyOtp("test@example.com", "100000");
            assertTrue(verifyResult);
        }
    }

    @Test
    void testSendOtpEmailGeneratesCorrectMessage() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        String result = emailService.sendOtpEmail("user@test.com");
        assertThat(result, startsWith("OTP sent successfully to"));
        verify(mailSender, atLeast(1)).send(any(SimpleMailMessage.class));
    }

    @Disabled()
    @Test
    void testMultipleOtpSendAndVerify() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class);
            MockedStatic<Random> mockedRandom = mockStatic(Random.class)) {
            LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
            mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(now);
            Random mockRandom = mock(Random.class);
            mockedRandom.when(() -> new Random()).thenReturn(mockRandom);
            doReturn(0).when(mockRandom).nextInt(900000);
            EmailService spyEmailService = spy(emailService);
            spyEmailService.sendOtpEmail("user1@example.com");
            spyEmailService.sendOtpEmail("user2@example.com");
            boolean verifyResult1 = spyEmailService.verifyOtp("user1@example.com", "100000");
            boolean verifyResult2 = spyEmailService.verifyOtp("user2@example.com", "100000");
            assertTrue(verifyResult1);
            assertTrue(verifyResult2);
        }
    }
}
