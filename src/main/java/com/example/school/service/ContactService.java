package com.example.school.service;

import com.example.school.config.ProCoderProps;
import com.example.school.constants.ProCoderConstants;
import com.example.school.model.Contact;
import com.example.school.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {
//    Logger log = LoggerFactory.getLogger(ContactService.class.getName());
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ProCoderProps proCoderProps;

    public boolean saveMessageDetails(Contact contact) {
        boolean isSaved = true;
        contact.setStatus(ProCoderConstants.OPEN);
        Contact result = contactRepository.save(contact);
        if(null!=result && result.getContact_id()>0){
            isSaved = true;
        }
        return isSaved;
    }

    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir){
        int pageSize = proCoderProps.getPageSize();//global page size
        if(null!=proCoderProps.getContact() && null!=proCoderProps.getContact().get("pageSize")){
            pageSize = Integer.parseInt(proCoderProps.getContact().get("pageSize").trim());
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, sortDir.equals("asc")?Sort.by(sortField).ascending():Sort.by(sortField).descending());
        Page<Contact> msgPage = contactRepository.findByStatusWithQuery(ProCoderConstants.OPEN,pageable);
        return msgPage;
    }

    public boolean updateMsgStatus(int contactId){
        boolean isUpdated = false;
        int rows = contactRepository.updateStatusById(ProCoderConstants.CLOSE,contactId);
        if(rows>0){
            isUpdated = true;
        }
        return isUpdated;
    }
}
