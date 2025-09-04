package com.unicksbyte.Online_food_ordering_app.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequest {

    private String foodId;

}
