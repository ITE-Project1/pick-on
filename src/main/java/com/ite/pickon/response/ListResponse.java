package com.ite.pickon.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse<T> {
    private List<T> list;
    private int totalPage;
}
