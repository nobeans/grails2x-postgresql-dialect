package grails2x.postgresql.dialect

class DomainC {

    String name

    static mapping = {
        id generator: 'sequence', params: [sequence:'domain_c_id_seq']
    }
}
