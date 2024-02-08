package org.zew.donations.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.zew.donations.model.Operation;
import org.zew.donations.model.OwnerType;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;
import org.zew.donations.model.request.WalletUpdateRequest;
import org.zew.donations.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    public static final BigDecimal ONE = BigDecimal.valueOf(1);
    public static final BigDecimal TWO = BigDecimal.valueOf(2);
    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    public void setUp() {
        lenient().when(walletRepository.save(any(Wallet.class))).then(answer -> {
            var wallet = (Wallet) answer.getArguments()[0];
            ReflectionTestUtils.setField(wallet, "walletId", UUID.randomUUID().toString());
            return wallet;
        });
    }

    @Test
    public void findById_NotFound() {
        // Arrange
        var id = UUID.randomUUID().toString();
        when(walletRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var thrown = assertThrows(RuntimeException.class, () -> walletService.findById(id));

        // Assert
        verify(walletRepository, times(1)).findById(id);
        assertEquals("Wallet not found", thrown.getMessage());
    }

    @Test
    public void getCountByMissionIdGroupByOwnerId_Success() {
        // Arrange
        String missionId = "m0001";
        when(walletRepository.getCountByMissionIdGroupByOwnerId(missionId)).thenReturn(1);

        // Act
        int donorsCount = walletService.getCountByMissionIdGroupByOwnerId(missionId);

        // Assert
        verify(walletRepository, times(1)).getCountByMissionIdGroupByOwnerId(missionId);
        assertEquals(1, donorsCount);
    }

    @Test
    public void findById_Success() {
        // Arrange
        var id = UUID.randomUUID().toString();
        var wallet = createWallet().setWalletId(id);
        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        // Act
        var foundWallet = walletService.findById(id);

        // Assert
        verify(walletRepository, times(1)).findById(id);
        assertEquals(wallet, foundWallet);
    }

    @Test
    public void createWallet_ValidWallet_Success() {
        // Arrange
        var wallet = createWallet();

        // Act
        var savedWallet = walletService.create(wallet);

        // Assert
        verify(walletRepository, times(1)).save(wallet);
        assertNotNull(savedWallet.getWalletId());
    }

    @Test
    public void createWallet_AlreadyExistentWallet_ThrowException() {
        // Arrange
        var wallet = createWallet();
        when(walletRepository.existsByOwnerIdAndType(wallet.getOwnerId(), wallet.getWalletType())).thenReturn(true);

        // Act
        var thrown = assertThrows(RuntimeException.class, () -> walletService.create(wallet));

        // Assert
        verify(walletRepository, times(1)).existsByOwnerIdAndType(wallet.getOwnerId(), wallet.getWalletType());
        verifyNoMoreInteractions(walletRepository);
        assertEquals("Wallet already exists", thrown.getMessage());
    }

    @Test
    public void updateWalletAmount_Incrementing_NotFound() {
        // Arrange
        var id = UUID.randomUUID().toString();
        var updateRequest = new WalletUpdateRequest(ONE, TWO, Operation.INCREMENT);
        when(walletRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var thrown = assertThrows(RuntimeException.class, () -> walletService.updateAmounts(id, updateRequest));

        // Assert
        verify(walletRepository, times(1)).findById(id);
        assertEquals("Wallet not found", thrown.getMessage());
    }

    @Test
    public void updateWalletAmount_Incrementing_Success() {
        // Arrange
        var id = UUID.randomUUID().toString();
        var wallet = createWallet().setWalletId(id);
        var walletForAsserting = createWallet().setWalletId(id);
        var updateRequest = new WalletUpdateRequest(ONE, TWO, Operation.INCREMENT);
        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        // Act
        var updatedWallet = walletService.updateAmounts(id, updateRequest);

        // Assert
        verify(walletRepository, times(1)).findById(id);
        verify(walletRepository, times(1)).save(any(Wallet.class));
        assertEquals(walletForAsserting.getAvailableAmount().add(ONE), updatedWallet.getAvailableAmount());
        assertEquals(walletForAsserting.getTotalAmount().add(TWO), updatedWallet.getTotalAmount());
    }

    @Test
    public void updateWalletAmount_Decrementing_Success() {
        // Arrange
        var id = UUID.randomUUID().toString();
        var wallet = createWallet().setWalletId(id);
        var walletForAsserting = createWallet().setWalletId(id);
        var updateRequest = new WalletUpdateRequest(ONE, TWO, Operation.DECREMENT);
        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        // Act
        var updatedWallet = walletService.updateAmounts(id, updateRequest);

        // Assert
        verify(walletRepository, times(1)).findById(id);
        verify(walletRepository, times(1)).save(any(Wallet.class));
        assertEquals(walletForAsserting.getAvailableAmount().subtract(ONE), updatedWallet.getAvailableAmount());
        assertEquals(walletForAsserting.getTotalAmount().subtract(TWO), updatedWallet.getTotalAmount());
    }

    private Wallet createWallet() {
        return Wallet
                .builder()
                .ownerId("u102john2021")
                .crmOwnerId("CRM111111john2021")
                .ownerType(OwnerType.IND)
                .missionId("m0001")
                .walletType(WalletType.IN_Mission)
                .availableAmount(BigDecimal.valueOf(10.0))
                .totalAmount(BigDecimal.valueOf(10.0))
                .currency("EUR")
                .build();
    }
}