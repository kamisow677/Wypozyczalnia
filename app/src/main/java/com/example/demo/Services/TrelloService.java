package com.example.demo.Services;

import com.example.demo.Configs.TrelloConfig;
import com.example.demo.Entities.Reader;
import com.example.demo.Repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrelloService {
    private final TrelloConfig trelloConfig;
    private final ReaderRepository readerRepository;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Sending database to trello");
        refreshUserList();
    }

    public void refreshUserList(){
        String boardId = getBoardsId(trelloConfig.getBOARD_NAME());
        List<JSONObject> boardsList = getList(boardId);
        postListOfReaders(boardsList, boardId);
    }

    private UriComponentsBuilder buildKeyTokenUriComponentBuilder(String url){
        return  UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(trelloConfig.getKEY_KEY(),trelloConfig.getKEY())
                .queryParam(trelloConfig.getKEY_TOKEN(), trelloConfig.getTOKEN());
    }

    private HttpHeaders createAcceptJsonHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private void postListOfReaders(List<JSONObject> listId, String idBoard) {
        RestTemplate restTemplate = new RestTemplate();
        final List<Reader> allReaders = readerRepository.findAll();

        Function<? super JSONObject, ?> getNames = k -> k.get("name");
        List<?> listNames = listId.stream().map(getNames).collect(Collectors.toList());

        allReaders.forEach(reader -> {
            if (!listNames.contains(reader.getFirstName() +"_"+reader.getLastName())) {
                UriComponentsBuilder builder = buildKeyTokenUriComponentBuilder(trelloConfig.getPOST_LIST_URL())
                        .queryParam("idBoard", idBoard)
                        .queryParam("name", reader.getFirstName() +"_"+reader.getLastName());

                HttpEntity<?> entity = new HttpEntity<>(createAcceptJsonHeaders());
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.POST,
                        entity,
                        String.class);
            }
        });
    }

    private String postNewCard(String description, String listId ,String title) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = buildKeyTokenUriComponentBuilder(trelloConfig.getPOST_NEW_CARD_URL())
                .queryParam("idList", listId)
                .queryParam("name",title)
                .queryParam("keepFromSource", "all")
                .queryParam("desc", description);

        HttpEntity<?> entity = new HttpEntity<>(createAcceptJsonHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);

        return response.toString();
    }

    private String getBoardsId(String boardName){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = buildKeyTokenUriComponentBuilder(trelloConfig.getGET_ALL_BOARDS_URL());

        HttpEntity<?> entity = new HttpEntity<>(createAcceptJsonHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        JSONArray jsonBodyArray = new JSONArray(response.getBody());
        JSONObject json = (JSONObject) (jsonBodyArray.get(0));
        return  (String) json.get(trelloConfig.getKEY_ID());
    }

    private List<JSONObject> getList(String boardId){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = buildKeyTokenUriComponentBuilder(trelloConfig.getGET_LIST_URL());

        HttpEntity<?> entity = new HttpEntity<>(createAcceptJsonHeaders());
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(trelloConfig.getKEY_ID(), boardId);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.buildAndExpand(pathParams).toUri(),
                HttpMethod.GET,
                entity,
                String.class);

        JSONArray jsonBodyArray = new JSONArray(response.getBody());

        List<JSONObject> list = IntStream.range(0, jsonBodyArray.length())
                .mapToObj(index -> (JSONObject) jsonBodyArray.get(index))
                .collect(Collectors.toList());

        return list;
    }

    public void postSuggestion(Long readerId, String suggestion) {
        String boardId = getBoardsId(trelloConfig.getBOARD_NAME());
        List<JSONObject> boardsList = getList(boardId);

        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "BAD ID"));
        JSONObject jsonObject = boardsList.stream()
                .filter(p->p.get("name").equals(reader.getFirstName() + "_"+ reader.getLastName()))
                .findFirst()
                .orElse(null);
        postNewCard(suggestion, (String) jsonObject.get(trelloConfig.getKEY_ID()), "Suggest");
    }

    public void postIndebtedness(Reader reader, String indebtedness) {
        log.info("Posted indebtedness {} for: {}",reader.getPunishment(),reader.getFirstName()+" "+reader.getLastName());
        String boardId = getBoardsId(trelloConfig.getBOARD_NAME());
        List<JSONObject> boardsList = getList(boardId);
        postListOfReaders(boardsList, boardId);

        JSONObject jsonObject = boardsList.stream()
                .filter(p->p.get("name").equals(reader.getFirstName() + "_"+ reader.getLastName()))
                .findFirst()
                .orElse(null);
        postNewCard("PLEASE_PAY_" + indebtedness, (String) jsonObject.get(trelloConfig.getKEY_ID()), "PAYMENT");
    }
}