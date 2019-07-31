package com.example.demo.unit

import com.example.demo.Configs.PunishmentConfig
import com.example.demo.DTOs.ReaderDTO
import com.example.demo.DemoApplication
import com.example.demo.Entities.Book
import com.example.demo.Entities.Reader
import com.example.demo.Exceptions.ReaderServiceException
import com.example.demo.Mappers.BookMapper
import com.example.demo.Mappers.ReaderMapper
import com.example.demo.Repositories.BookRepository
import com.example.demo.Repositories.ReaderRepository
import com.example.demo.Services.ReaderService
import com.example.demo.Services.ReaderServiceImpl
import com.example.demo.Services.TrelloService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest(classes = DemoApplication.class)
class ReaderServiceImplTest extends  spock.lang.Specification{

//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule()

    BookRepository bookRepository

    ReaderRepository readerRepository

    @Autowired
    ReaderMapper readerMapper

    @Autowired
    BookMapper bookMapper

    TrelloService trelloService

    PunishmentConfig punishmentConfig

    ReaderService readerService

    def reader

    void setup(){
        punishmentConfig = Stub()
        punishmentConfig.getCRITICAL_PUNISHMENT() >> 18.0
        punishmentConfig.getDAYS_TO_READ() >> 30
        punishmentConfig.getNORMAL_LENGTH() >> 7
        punishmentConfig.getINTEREST_PRICE() >> 0.5
        punishmentConfig.getBASE_PRICE() >> 2.0
        trelloService = Mock()
        bookRepository = Mock()

        def book = new Book("Iliada","Homer")
        reader = new Reader("Karolo","Krawczyk",new BigDecimal("0.0"))
        book.setReturnDate(LocalDate.parse("2019-06-30"))
        book.setUpdatePunishmentDate(LocalDate.parse("2019-06-30"))
        book.setReader(reader)
    }

    @Test
    void "testSaveReader" (){
        setup:
        readerRepository = Stub(ReaderRepository)
        readerRepository.save(_) >> reader
        readerService = new ReaderServiceImpl(readerRepository,bookRepository
                ,readerMapper,bookMapper,trelloService,punishmentConfig)

        when:
        def readerReturned = readerService.saveReader(new ReaderDTO("Karolo","Krawczyk",new BigDecimal("0.0"),null))

        then:
        1 * trelloService.refreshUserList()
        readerReturned.getFirstName() == "Karolo"
        readerReturned.getLastName() == "Krawczyk"
    }

    @Test
    void "testGetReader" (){
        given:
        readerRepository = Mock(ReaderRepository)
        1 * readerRepository.findById(_) >> new Optional(reader)
        readerService = new ReaderServiceImpl(readerRepository,bookRepository
                ,readerMapper,bookMapper,trelloService,punishmentConfig)

        when:
        def readerReturned = readerService.getReaderById(2)

        then:
        readerReturned.getFirstName() == "Karolo"
        readerReturned.getLastName() == "Krawczyk"
    }
}
