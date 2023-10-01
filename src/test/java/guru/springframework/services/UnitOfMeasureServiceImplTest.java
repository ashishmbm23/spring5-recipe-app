package guru.springframework.services;

import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    public static final long ID = 1L;
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @Mock
    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    @InjectMocks
    UnitOfMeasureServiceImpl unitOfMeasureService;
    @Test
    void listAllUOMs() {
        Set<UnitOfMeasure> unitOfMeasureSet = getUnitOfMeasures();
        when( unitOfMeasureRepository.findAll() ).thenReturn(unitOfMeasureSet);

        UnitOfMeasureCommand unitOfMeasureCommand = getUnitOfMeasureCommand();
        when( unitOfMeasureToUnitOfMeasureCommand.convert(any())).thenReturn(unitOfMeasureCommand);

        Set<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.listAllUOMs();
        assertNotNull( unitOfMeasureCommands );
        UnitOfMeasureCommand resultUnitOfMeasureCommand = unitOfMeasureCommands.iterator().next();
        assertEquals( 1, unitOfMeasureCommands.size() );
        assertEquals( ID, resultUnitOfMeasureCommand.getId());
        verify( unitOfMeasureRepository, times(1)).findAll();
        verify( unitOfMeasureToUnitOfMeasureCommand, times(1)).convert(any());
    }

    private static UnitOfMeasureCommand getUnitOfMeasureCommand() {
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(ID);
        return unitOfMeasureCommand;
    }

    private static Set<UnitOfMeasure> getUnitOfMeasures() {
        Set<UnitOfMeasure> unitOfMeasureSet = new HashSet<>();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(ID);
        unitOfMeasureSet.add( unitOfMeasure );
        return unitOfMeasureSet;
    }
}