package com.talentflow.api.service;

import com.talentflow.api.entity.Client;
import com.talentflow.api.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client>getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(existing -> {
                    existing.setClientCode(updatedClient.getClientCode());
                    existing.setClientName(updatedClient.getClientName());
                    existing.setIndustry(updatedClient.getIndustry());
                    existing.setContactName(updatedClient.getContactName());
                    existing.setContactEmail(updatedClient.getContactEmail());
                    existing.setContactPhone(updatedClient.getContactPhone());
                    existing.setNotes(updatedClient.getNotes());
                    existing.setIsActive(updatedClient.getIsActive());
                    existing.setUpdatedAt(updatedClient.getUpdatedAt());
                    return clientRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
