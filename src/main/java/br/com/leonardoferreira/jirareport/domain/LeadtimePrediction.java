package br.com.leonardoferreira.jirareport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leferreira
 * @since 3/20/18 10:34 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadtimePrediction {

    private String size;

    private Double leadtime;

}