package com.sapient.controller;

import com.sapient.controller.record.DeleteSuccess;
import com.sapient.controller.record.FailurePayload;
import com.sapient.model.beans.Category;
import com.sapient.model.beans.Merchant;
import com.sapient.model.service.CategoryService;
import com.sapient.model.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    record MerchantSuccess(Merchant merchant) {}

    @QueryMapping
    public Record merchant(@Argument String passwordHash, @Argument Integer id){
        try{
            return new MerchantSuccess(merchantService.getMerchant(passwordHash, id));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    record MerchantsSuccess(List<Merchant> merchants) {}

    @QueryMapping
    public Record merchants(@Argument String passwordHash){
        try{
            return new MerchantsSuccess(merchantService.getMerchants(passwordHash));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record createMerchant(@Argument String passwordHash, @Argument String name, @Argument String description, @Argument Integer defaultCategoryId) {
        try{
            return new MerchantSuccess(merchantService.createMerchant(passwordHash, name, description, defaultCategoryId));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteMerchant(@Argument String passwordHash, @Argument Integer id){
        try{
            merchantService.deleteMerchant(passwordHash, id);
            return new DeleteSuccess("Successfully deleted merchant");
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
