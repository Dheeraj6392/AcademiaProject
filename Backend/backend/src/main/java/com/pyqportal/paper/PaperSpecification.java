package com.pyqportal.paper;

import org.springframework.data.jpa.domain.Specification;

public final class PaperSpecification {

    private PaperSpecification() {}

    public static Specification<Paper> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<Paper> hasBranch(Branch branch) {
        return (root, query, cb) ->
                branch == null ? cb.conjunction() : cb.equal(root.get("branch"), branch);
    }

    public static Specification<Paper> hasSubject(String subject) {
        return (root, query, cb) ->
                subject == null ? cb.conjunction() : cb.equal(root.get("subject"), subject);
    }

    public static Specification<Paper> hasYear(Integer year) {
        return (root, query, cb) ->
                year == null ? cb.conjunction() : cb.equal(root.get("year"), year);
    }

    public static Specification<Paper> hasExamType(ExamType examType) {
        return (root, query, cb) ->
                examType == null ? cb.conjunction() : cb.equal(root.get("examType"), examType);
    }
}
