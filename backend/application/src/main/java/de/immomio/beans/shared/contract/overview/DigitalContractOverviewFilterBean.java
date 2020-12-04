package de.immomio.beans.shared.contract.overview;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.beans.shared.contract.DigitalContractSimpleState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Hansen
 */

@Getter
@Setter
public class DigitalContractOverviewFilterBean extends CustomPageable implements Serializable {

    private static final long serialVersionUID = 5602121425364990868L;

    private List<DigitalContractSimpleState> states = new ArrayList<>();

    private List<Long> agents = new ArrayList<>();
}
