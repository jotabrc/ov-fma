package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PartyDto;
import io.github.jotabrc.ov_fma_finance.model.Party;
import io.github.jotabrc.ov_fma_finance.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;

    @Autowired
    public PartyServiceImpl(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    /**
     * Add new Party.
     * @param dto Party data.
     * @return Party UUID.
     */
    @Override
    public String addParty(final PartyDto dto) {
        Party party = partyRepository.findByName(dto.getName())
                .orElse(buildNewParty(dto));

        return partyRepository.save(party).getUuid();
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Build new Party to be persisted.
     * @param dto Party data.
     * @return Party object.
     */
    private Party buildNewParty(PartyDto dto) {
        return Party
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(dto.getName())
                .build();
    }
}
