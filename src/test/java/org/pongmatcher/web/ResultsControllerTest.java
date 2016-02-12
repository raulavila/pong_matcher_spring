package org.pongmatcher.web;

import org.junit.Test;
import org.pongmatcher.domain.Result;
import org.pongmatcher.repositories.ResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public final class ResultsControllerTest {

    private final ResultRepository resultRepository = mock(ResultRepository.class);

    private final ResultsController resultsController = new ResultsController(resultRepository);

    private final MockMvc mockMvc = standaloneSetup(this.resultsController).build();

    @Test
    public void testResultsPostAPI() throws Exception {
        this.mockMvc.perform(post("/results")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"winner\": \"test-winner\", \"loser\": \"test-loser\", \"match_id\": \"test-match-id\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"winner\":\"test-winner\",\"loser\":\"test-loser\",\"match_id\":\"test-match-id\"}"));
    }

    @Test
    public void testSaveResult() {
        Result result = new Result("test-winner", "test-loser", "test-match-id");
        ResponseEntity<Result> response = this.resultsController.save(result);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response.getBody(), result);
    }

    @Test
    public void testResultIsPersisted_whenSaving() throws Exception {
        Result result = new Result("test-winner", "test-loser", "test-match-id");

        this.resultsController.save(result);

        verify(this.resultRepository).saveAndFlush(any(Result.class));
    }
}

