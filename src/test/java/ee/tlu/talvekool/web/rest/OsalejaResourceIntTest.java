package ee.tlu.talvekool.web.rest;

import ee.tlu.talvekool.TalvekoolApp;

import ee.tlu.talvekool.domain.Osaleja;
import ee.tlu.talvekool.repository.OsalejaRepository;
import ee.tlu.talvekool.service.OsalejaService;
import ee.tlu.talvekool.repository.search.OsalejaSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ee.tlu.talvekool.domain.enumeration.Sugu;
import ee.tlu.talvekool.domain.enumeration.Oskustase;
/**
 * Test class for the OsalejaResource REST controller.
 *
 * @see OsalejaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalvekoolApp.class)
public class OsalejaResourceIntTest {

    private static final String DEFAULT_NIMI = "AAAAAAAAAA";
    private static final String UPDATED_NIMI = "BBBBBBBBBB";

    private static final Sugu DEFAULT_SUGU = Sugu.mees;
    private static final Sugu UPDATED_SUGU = Sugu.naine;

    private static final Oskustase DEFAULT_OSKUSTASE = Oskustase.algaja;
    private static final Oskustase UPDATED_OSKUSTASE = Oskustase.keskmine;

    @Inject
    private OsalejaRepository osalejaRepository;

    @Inject
    private OsalejaService osalejaService;

    @Inject
    private OsalejaSearchRepository osalejaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOsalejaMockMvc;

    private Osaleja osaleja;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OsalejaResource osalejaResource = new OsalejaResource();
        ReflectionTestUtils.setField(osalejaResource, "osalejaService", osalejaService);
        this.restOsalejaMockMvc = MockMvcBuilders.standaloneSetup(osalejaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Osaleja createEntity(EntityManager em) {
        Osaleja osaleja = new Osaleja()
                .nimi(DEFAULT_NIMI)
                .sugu(DEFAULT_SUGU)
                .oskustase(DEFAULT_OSKUSTASE);
        return osaleja;
    }

    @Before
    public void initTest() {
        osalejaSearchRepository.deleteAll();
        osaleja = createEntity(em);
    }

    @Test
    @Transactional
    public void createOsaleja() throws Exception {
        int databaseSizeBeforeCreate = osalejaRepository.findAll().size();

        // Create the Osaleja

        restOsalejaMockMvc.perform(post("/api/osalejas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(osaleja)))
            .andExpect(status().isCreated());

        // Validate the Osaleja in the database
        List<Osaleja> osalejas = osalejaRepository.findAll();
        assertThat(osalejas).hasSize(databaseSizeBeforeCreate + 1);
        Osaleja testOsaleja = osalejas.get(osalejas.size() - 1);
        assertThat(testOsaleja.getNimi()).isEqualTo(DEFAULT_NIMI);
        assertThat(testOsaleja.getSugu()).isEqualTo(DEFAULT_SUGU);
        assertThat(testOsaleja.getOskustase()).isEqualTo(DEFAULT_OSKUSTASE);

        // Validate the Osaleja in ElasticSearch
        Osaleja osalejaEs = osalejaSearchRepository.findOne(testOsaleja.getId());
        assertThat(osalejaEs).isEqualToComparingFieldByField(testOsaleja);
    }

    @Test
    @Transactional
    public void checkNimiIsRequired() throws Exception {
        int databaseSizeBeforeTest = osalejaRepository.findAll().size();
        // set the field null
        osaleja.setNimi(null);

        // Create the Osaleja, which fails.

        restOsalejaMockMvc.perform(post("/api/osalejas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(osaleja)))
            .andExpect(status().isBadRequest());

        List<Osaleja> osalejas = osalejaRepository.findAll();
        assertThat(osalejas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSuguIsRequired() throws Exception {
        int databaseSizeBeforeTest = osalejaRepository.findAll().size();
        // set the field null
        osaleja.setSugu(null);

        // Create the Osaleja, which fails.

        restOsalejaMockMvc.perform(post("/api/osalejas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(osaleja)))
            .andExpect(status().isBadRequest());

        List<Osaleja> osalejas = osalejaRepository.findAll();
        assertThat(osalejas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOskustaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = osalejaRepository.findAll().size();
        // set the field null
        osaleja.setOskustase(null);

        // Create the Osaleja, which fails.

        restOsalejaMockMvc.perform(post("/api/osalejas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(osaleja)))
            .andExpect(status().isBadRequest());

        List<Osaleja> osalejas = osalejaRepository.findAll();
        assertThat(osalejas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOsalejas() throws Exception {
        // Initialize the database
        osalejaRepository.saveAndFlush(osaleja);

        // Get all the osalejas
        restOsalejaMockMvc.perform(get("/api/osalejas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(osaleja.getId().intValue())))
            .andExpect(jsonPath("$.[*].nimi").value(hasItem(DEFAULT_NIMI.toString())))
            .andExpect(jsonPath("$.[*].sugu").value(hasItem(DEFAULT_SUGU.toString())))
            .andExpect(jsonPath("$.[*].oskustase").value(hasItem(DEFAULT_OSKUSTASE.toString())));
    }

    @Test
    @Transactional
    public void getOsaleja() throws Exception {
        // Initialize the database
        osalejaRepository.saveAndFlush(osaleja);

        // Get the osaleja
        restOsalejaMockMvc.perform(get("/api/osalejas/{id}", osaleja.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(osaleja.getId().intValue()))
            .andExpect(jsonPath("$.nimi").value(DEFAULT_NIMI.toString()))
            .andExpect(jsonPath("$.sugu").value(DEFAULT_SUGU.toString()))
            .andExpect(jsonPath("$.oskustase").value(DEFAULT_OSKUSTASE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOsaleja() throws Exception {
        // Get the osaleja
        restOsalejaMockMvc.perform(get("/api/osalejas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOsaleja() throws Exception {
        // Initialize the database
        osalejaService.save(osaleja);

        int databaseSizeBeforeUpdate = osalejaRepository.findAll().size();

        // Update the osaleja
        Osaleja updatedOsaleja = osalejaRepository.findOne(osaleja.getId());
        updatedOsaleja
                .nimi(UPDATED_NIMI)
                .sugu(UPDATED_SUGU)
                .oskustase(UPDATED_OSKUSTASE);

        restOsalejaMockMvc.perform(put("/api/osalejas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOsaleja)))
            .andExpect(status().isOk());

        // Validate the Osaleja in the database
        List<Osaleja> osalejas = osalejaRepository.findAll();
        assertThat(osalejas).hasSize(databaseSizeBeforeUpdate);
        Osaleja testOsaleja = osalejas.get(osalejas.size() - 1);
        assertThat(testOsaleja.getNimi()).isEqualTo(UPDATED_NIMI);
        assertThat(testOsaleja.getSugu()).isEqualTo(UPDATED_SUGU);
        assertThat(testOsaleja.getOskustase()).isEqualTo(UPDATED_OSKUSTASE);

        // Validate the Osaleja in ElasticSearch
        Osaleja osalejaEs = osalejaSearchRepository.findOne(testOsaleja.getId());
        assertThat(osalejaEs).isEqualToComparingFieldByField(testOsaleja);
    }

    @Test
    @Transactional
    public void deleteOsaleja() throws Exception {
        // Initialize the database
        osalejaService.save(osaleja);

        int databaseSizeBeforeDelete = osalejaRepository.findAll().size();

        // Get the osaleja
        restOsalejaMockMvc.perform(delete("/api/osalejas/{id}", osaleja.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean osalejaExistsInEs = osalejaSearchRepository.exists(osaleja.getId());
        assertThat(osalejaExistsInEs).isFalse();

        // Validate the database is empty
        List<Osaleja> osalejas = osalejaRepository.findAll();
        assertThat(osalejas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOsaleja() throws Exception {
        // Initialize the database
        osalejaService.save(osaleja);

        // Search the osaleja
        restOsalejaMockMvc.perform(get("/api/_search/osalejas?query=id:" + osaleja.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(osaleja.getId().intValue())))
            .andExpect(jsonPath("$.[*].nimi").value(hasItem(DEFAULT_NIMI.toString())))
            .andExpect(jsonPath("$.[*].sugu").value(hasItem(DEFAULT_SUGU.toString())))
            .andExpect(jsonPath("$.[*].oskustase").value(hasItem(DEFAULT_OSKUSTASE.toString())));
    }
}
