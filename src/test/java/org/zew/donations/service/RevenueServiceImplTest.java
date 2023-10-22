package org.zew.donations.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.zew.donations.model.Revenue;
import org.zew.donations.repository.RevenueRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RevenueServiceImplTest {

    @InjectMocks
    private RevenueServiceImpl revenueService;

    @Mock
    private RevenueRepository revenueRepository;

    @BeforeEach
    public void setUp() {
        lenient().when(revenueRepository.save(any(Revenue.class))).then(answer -> {
            var revenue = (Revenue) answer.getArguments()[0];
            ReflectionTestUtils.setField(revenue, "revenueId", UUID.randomUUID().toString());
            return revenue;
        });
    }

    @Test
    public void findById_NotFound() {
        // Arrange
        var id = UUID.randomUUID().toString();
        when(revenueRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var thrown = assertThrows(RuntimeException.class, () -> revenueService.findById(id));

        // Assert
        verify(revenueRepository, times(1)).findById(id);
        assertEquals("Revenue not found", thrown.getMessage());
    }

    @Test
    public void findById_Success() {
        // Arrange
        var id = UUID.randomUUID().toString();
        var revenue = createRevenue().setRevenueId(id);
        when(revenueRepository.findById(id)).thenReturn(Optional.of(revenue));

        // Act
        var foundRevenue = revenueService.findById(id);

        // Assert
        verify(revenueRepository, times(1)).findById(id);
        assertEquals(revenue, foundRevenue);
    }

    @Test
    public void createRevenue_ValidRevenue_Success() {
        // Arrange
        var revenue = createRevenue();

        // Act
        var savedRevenue = revenueService.create(revenue);

        // Assert
        verify(revenueRepository, times(1)).save(revenue);
        assertNotNull(savedRevenue.getRevenueId());
    }

    @Test
    public void createRevenue_AlreadyExistentRevenue_ThrowException() {
        // Arrange
        var revenue = createRevenue();
        when(revenueRepository.existsById(revenue.getRevenueId())).thenReturn(true);

        // Act
        var thrown = assertThrows(RuntimeException.class, () -> revenueService.create(revenue));

        // Assert
        verify(revenueRepository, times(1)).existsById(revenue.getRevenueId());
        verifyNoMoreInteractions(revenueRepository);
        assertEquals("Revenue already exists", thrown.getMessage());
    }

    private Revenue createRevenue() {
        // You can fill in the details for creating a Revenue object here.
        return new Revenue();
    }
}
