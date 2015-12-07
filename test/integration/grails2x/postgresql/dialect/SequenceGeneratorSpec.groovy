package grails2x.postgresql.dialect

import grails.transaction.Transactional
import spock.lang.Specification

@Transactional
class SequenceGeneratorSpec extends Specification {

    void "id should be independently generated"() {
        when:
        3.times { new DomainA(name: "Mike", value: "x", infinite: "x").save(failOnError: true, flush: true) }
        3.times { new DomainB(name: "David").save(failOnError: true, flush: true) }
        3.times { new DomainC(name: "Junko").save(failOnError: true, flush: true) }

        then:
        DomainA.list()*.id == [1, 2, 3]
        DomainB.list()*.id == [1, 2, 3]
        DomainC.list()*.id == [1, 2, 3]
    }
}
