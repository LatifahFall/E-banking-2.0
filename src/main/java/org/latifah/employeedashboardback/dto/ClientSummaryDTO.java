package org.latifah.employeedashboardback.dto;

import org.latifah.employeedashboardback.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ClientSummaryDTO {
    private Long clientId;
    private String fullName;
    private List<AccountSummaryDTO> accounts;
    private Boolean compteBloque;
    private Boolean documentsComplets;

    // constructeurs, getters/setters
    public ClientSummaryDTO() {
        super();
    }
    public ClientSummaryDTO(Long clientId, String fullName, List<AccountSummaryDTO> accounts) {
        this.clientId = clientId;
        this.fullName = fullName;
        this.accounts = accounts;
    }

    public static ClientSummaryDTO fromUser(User user) {
        ClientSummaryDTO dto = new ClientSummaryDTO();

        dto.setClientId(user.getId());
        dto.setFullName(user.getFirstName() + " " + user.getLastName());

        dto.setAccounts(
                user.getAccounts().stream().map(account -> {
                    AccountSummaryDTO accDto = new AccountSummaryDTO();
                    accDto.setAccountNumber(account.getAccountNumber());
                    accDto.setType(account.getType());
                    accDto.setBalance(account.getBalance());

                    accDto.setAccountOperations(
                            account.getAccountOperations().stream().map(op -> {
                                AccountOperationDTO opDto = new AccountOperationDTO();
                                opDto.setId(op.getId());
                                opDto.setAmount(op.getAmount());
                                opDto.setOperationDate(op.getOperationDate());
                                opDto.setType(op.getType());
                                opDto.setDescription(op.getDescription());
                                return opDto;
                            }).collect(Collectors.toList())
                    );

                    return accDto;
                }).collect(Collectors.toList())
        );

        return dto;
    }



    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public List<AccountSummaryDTO> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<AccountSummaryDTO> accounts) {
        this.accounts = accounts;
    }

    public Boolean getCompteBloque() {
        return compteBloque;
    }
    public void setCompteBloque(Boolean compteBloque) {
        this.compteBloque = compteBloque;
    }
    public Boolean getDocumentsComplets() {
        return documentsComplets;
    }
    public void setDocumentsComplets(Boolean documentsComplets) {
        this.documentsComplets = documentsComplets;
    }
}
