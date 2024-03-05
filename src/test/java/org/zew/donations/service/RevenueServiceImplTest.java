package org.zew.donations.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.zew.donations.controller.exception.RevenueAlreadyExistsException;
import org.zew.donations.model.Revenue;
import org.zew.donations.repository.RevenueRepository;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
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

    private List<Revenue> revenues;


    @BeforeEach
    public void setUp() throws StreamReadException, DatabindException, IOException {
        
        // Load the revenues from the revenues.json file
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/revenues.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        revenues = objectMapper.readValue(reader, new TypeReference<List<Revenue>>() {});


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
    public void findById_Success() throws StreamReadException, DatabindException, IOException {
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
    public void createRevenue_ValidRevenue_Success() throws StreamReadException, DatabindException, IOException, RevenueAlreadyExistsException {
        // Arrange
        var revenue = createRevenue();

        // Act
        var savedRevenue = revenueService.create(revenue);

        // Assert
        verify(revenueRepository, times(1)).save(revenue);
        assertNotNull(savedRevenue.getRevenueId());
    }

    @Test
    public void createRevenue_AlreadyExistentRevenue_ThrowException() throws StreamReadException, DatabindException, IOException {
        // Arrange
        var revenue = createRevenue();
        when(revenueRepository.existsById(revenue.getRevenueId())).thenReturn(true);

        // Act
        var thrown = assertThrows(RevenueAlreadyExistsException.class, () -> revenueService.create(revenue));

        // Assert
        verify(revenueRepository, times(1)).existsById(revenue.getRevenueId());
        verifyNoMoreInteractions(revenueRepository);
        assertEquals("Revenue already exists", thrown.getMessage());
    }

    private Revenue createRevenue() throws IOException {
        return revenues.get(0);
    }


    /* ToDo: All tests need to be revised as it should insert data in QLDB and retrieve data as well */
    @Test
    public void testFindByOwnerId() {
        List<Revenue> ownerRevenues = revenues; // Assuming all revenues in the JSON have the same ownerId for this example

        when(revenueRepository.findByOwnerId(revenues.get(0).getOwnerId())).thenReturn(ownerRevenues);

        List<Revenue> foundRevenues = revenueService.getAllRevenuesByOwnerId(revenues.get(0).getOwnerId());
        assertEquals(ownerRevenues.size(), foundRevenues.size());
        assertEquals(ownerRevenues, foundRevenues);
    }

}
