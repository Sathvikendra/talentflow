package com.talentflow.api.controller;

import com.talentflow.api.dto.ClientRequest;
import com.talentflow.api.dto.ClientResponse;
import com.talentflow.api.entity.Client;
import com.talentflow.api.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientResponse> getAllClients() {
        return clientService.getAllClients().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClientResponse getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @PostMapping
    public ClientResponse createClient(@RequestBody ClientRequest request) {
        Client client = toEntity(request);
        return toResponse(clientService.createClient(client));
    }

    @PutMapping("/{id}")
    public ClientResponse updateClient(@PathVariable Long id, @RequestBody ClientRequest request) {
        Client client = toEntity(request);
        return toResponse(clientService.updateClient(id, client));
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }

    // Helper methods for mapping
    private ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setClientCode(client.getClientCode());
        response.setClientName(client.getClientName());
        response.setIndustry(client.getIndustry());
        response.setContactName(client.getContactName());
        response.setContactEmail(client.getContactEmail());
        response.setContactPhone(client.getContactPhone());
        response.setNotes(client.getNotes());
        response.setIsActive(client.getIsActive());
        response.setCreatedAt(client.getCreatedAt());
        response.setUpdatedAt(client.getUpdatedAt());
        return response;
    }

    private Client toEntity(ClientRequest request) {
        Client client = new Client();
        client.setClientCode(request.getClientCode());
        client.setClientName(request.getClientName());
        client.setIndustry(request.getIndustry());
        client.setContactName(request.getContactName());
        client.setContactEmail(request.getContactEmail());
        client.setContactPhone(request.getContactPhone());
        client.setNotes(request.getNotes());
        client.setIsActive(request.getIsActive());
        return client;
    }
}


