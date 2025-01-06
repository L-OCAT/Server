package com.locat.api.infra.aws;

import com.locat.api.infra.aws.config.AwsProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractLocatAwsClient {

  protected final AwsProperties awsProperties;
}
