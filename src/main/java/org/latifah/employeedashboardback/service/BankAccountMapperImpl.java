package org.latifah.employeedashboardback.service;

import org.latifah.employeedashboardback.dto.AccountOperationDTO;
import org.latifah.employeedashboardback.dto.ClientDTO;
import org.latifah.employeedashboardback.dto.CurrentBankAccountDTO;
import org.latifah.employeedashboardback.dto.SavingBankAccountDTO;
import org.latifah.employeedashboardback.model.AccountOperation;
import org.latifah.employeedashboardback.model.CurrentAccount;
import org.latifah.employeedashboardback.model.SavingAccount;
import org.latifah.employeedashboardback.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public ClientDTO fromClient(User client) {
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);
        return clientDTO;
    }

    public User fromClientDTO(ClientDTO clientDTO) {
        User client = new User();
        BeanUtils.copyProperties(clientDTO, client);
        return client;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, dto);
        dto.setClient(fromClient(savingAccount.getUser()));
        dto.setType(savingAccount.getClass().getSimpleName());
        return dto;
    }

    public SavingAccount fromSavingAccountBankAccountDTO(SavingBankAccountDTO dto) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(dto, savingAccount);
        savingAccount.setUser(fromClientDTO(dto.getClient()));
        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, dto);
        dto.setClient(fromClient(currentAccount.getUser()));
        dto.setType(currentAccount.getClass().getSimpleName());
        return dto;
    }

    public CurrentAccount fromCurrentAccountBankAccountDTO(CurrentBankAccountDTO dto) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(dto, currentAccount);
        currentAccount.setUser(fromClientDTO(dto.getClient()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, dto);
        return dto;
    }
}
