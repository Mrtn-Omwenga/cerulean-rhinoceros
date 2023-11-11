package org.zew.donations.repository;

import org.springframework.stereotype.Repository;
import org.zew.donations.commons.repository.AbstractQldbQldbRepository;
import org.zew.donations.model.Revenue;

@Repository
public class RevenuesRepositoryImpl extends AbstractQldbQldbRepository<Revenue> implements RevenuesRepository {}
