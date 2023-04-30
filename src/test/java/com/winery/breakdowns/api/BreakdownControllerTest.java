package com.winery.breakdowns.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BreakdownControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void testGetBreakdownByYear_validLotCode_expect200WithData() throws Exception {
        String expectedResponse = readSampleFile("samples/get-by-year-response.json");

        this.mockMvc.perform(get("/api/breakdown/year/11YVCHAR001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void testGetBreakdownByVariety_validLotCode_expect200WithData() throws Exception {
        String expectedResponse = readSampleFile("samples/get-by-variety-response.json");

        this.mockMvc.perform(get("/api/breakdown/variety/11YVCHAR002"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void testGetBreakdownByRegion_validLotCode_expect200WithData() throws Exception {
        String expectedResponse = readSampleFile("samples/get-by-region-response.json");

        this.mockMvc.perform(get("/api/breakdown/region/15MPPN002-VK"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void testGetBreakdownByYearAndVariety_validLotCode_expect200WithData() throws Exception {
        String expectedResponse = readSampleFile("samples/get-by-year-variety-response.json");

        this.mockMvc.perform(get("/api/breakdown/year-variety/15MPPN002-VK"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void testGetBreakdownByYear_inValidLotCode_expect404() throws Exception {

        this.mockMvc.perform(get("/api/breakdown/year/INVALID-VK"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    String readSampleFile(String filePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = Objects.requireNonNull(classLoader.getResource(filePath), "Couldn't find file: " + filePath);
        try (Stream<String> lines = Files.lines(Paths.get(url.getPath()))) {
            return lines.collect(Collectors.joining("\n"));
        }
    }
}
