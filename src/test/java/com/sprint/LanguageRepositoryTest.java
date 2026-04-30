package com.sprint;

import com.sprint.Entities.Language;
import com.sprint.Repository.LanguageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void testFindAll_returnsLanguages() {
        List<Language> languages = languageRepository.findAll();

        assertFalse(languages.isEmpty());
        assertNotNull(languages.get(0).getLanguageId());
        assertNotNull(languages.get(0).getName());
    }

    
    @Test
    public void testSave_newLanguage() {
        Language newLanguage = new Language();
        newLanguage.setName("Test Language");
        newLanguage.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        Language savedLanguage = languageRepository.save(newLanguage);

        assertNotNull(savedLanguage.getLanguageId());
        assertEquals("Test Language", savedLanguage.getName());
    }

    @Test
    public void testSave_updateLanguage() {
        Optional<Language> languageOpt = languageRepository.findById(1L);
        assertTrue(languageOpt.isPresent());

        Language language = languageOpt.get();
        String originalName = language.getName();
        language.setName("Updated Name");

        Language updatedLanguage = languageRepository.save(language);

        assertEquals("Updated Name", updatedLanguage.getName());
        // Reset for transaction rollback
        language.setName(originalName);
    }

}