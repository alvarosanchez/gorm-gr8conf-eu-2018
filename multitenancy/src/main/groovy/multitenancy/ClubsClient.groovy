package multitenancy

import io.micronaut.http.client.Client

@Client("/")
interface ClubsClient extends ClubsApi {}