//
//
//package com.example.demo
//import com.example.demo.Entities.Book
//import com.example.demo.Entities.Reader
//import com.example.demo.Repositories.BookRepository
//import com.example.demo.Repositories.ReaderRepository
//import com.example.demo.Services.BookService
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.junit.MockitoJUnit
//import org.mockito.junit.MockitoRule
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = DemoApplication.class)
//class test {
//
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule()
//
//    @Mock
//    private ReaderRepository readerRepository
//
//    @Mock
//    private BookRepository bookRepository
//
//    @InjectMocks
//    BookService bookService
//
//    @Autowired
//    ObjectMapper objectMapper
//
//    @Test
//    void "test" (){
//
//        setup:
//        Book book = new Book("Iliada","Homer", "2019-06-30","2019-06-20"
//                , new Reader("Karolo","Krawczyk",new BigDecimal("0.0")))
//        bookRepository.findByIdAndReaderIsNotNull() >> book
//
//        when:
//        bookService.updatePunishment();
//
//        then:
//
//        expect:
//        book.getReader().getPunishment() == 18.5;
//
//
////        setup:
////        def mock = new MockFor(ReaderRepository)
////        mock.findById
////
////        readerRepository.
////        controller.detailService = Mock(DetailService) {
////            getDetails(_) >> {
////                throw new DetailsNotFoundException();
////            }
////        }
////
////        when:
////        def response = mockMvc.perform(request);
////
////        then:
////        response.andExpect(status().isBadRequest())
//
//    }
//}
