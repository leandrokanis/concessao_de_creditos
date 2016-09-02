package concessao

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class InvoiceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Invoice.list(params), model:[invoiceCount: Invoice.count()]
    }

    def show(Invoice invoice) {
        respond invoice
    }

    def create() {
        respond new Invoice(params)
    }

    @Transactional
    def save(Invoice invoice) {
        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (invoice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond invoice.errors, view:'create'
            return
        }

        invoice.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect invoice
            }
            '*' { respond invoice, [status: CREATED] }
        }
    }

    def edit(Invoice invoice) {
        respond invoice
    }

    @Transactional
    def update(Invoice invoice) {
        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (invoice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond invoice.errors, view:'edit'
            return
        }

        invoice.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect invoice
            }
            '*'{ respond invoice, [status: OK] }
        }
    }

    @Transactional
    def delete(Invoice invoice) {

        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        invoice.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
