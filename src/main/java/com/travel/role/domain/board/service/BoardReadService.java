package com.travel.role.domain.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.board.repository.BookInfoRepository;
import com.travel.role.global.exception.board.AccountingInfoNotFoundException;
import com.travel.role.global.exception.board.BookInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReadService {
    private final AccountingInfoRepository accountingInfoRepository;
    private final BookInfoRepository bookInfoRepository;

    public BookInfo findBookInfoByIdOrElseThrow(Long bookInfoId) {

        return bookInfoRepository.findById(bookInfoId)
                .orElseThrow(BookInfoNotFoundException::new);
    }

    public AccountingInfo findAccountingInfoByIdOrElseThrow(Long accountingInfoId) {

        return accountingInfoRepository.findById(accountingInfoId)
                .orElseThrow(AccountingInfoNotFoundException::new);
    }
}
